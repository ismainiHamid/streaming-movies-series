package ma.streaming.upload.shared.utils;

import jakarta.annotation.PreDestroy;
import ma.streaming.upload.notification.NotificationService;
import ma.streaming.upload.notification.dto.NotificationResponse;
import ma.streaming.upload.shared.builder.FFmpegBuilder;
import ma.streaming.upload.shared.builder.ProgressBuilder;
import ma.streaming.upload.shared.enums.StatusEnum;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.storage.MinioService;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
@Scope("singleton")
public class ConvertingUtil {
    private final MinioService minioService;
    private final ExecutorService executorService;
    private final NotificationService notificationService;

    public ConvertingUtil(MinioService minioService, ExecutorService executorService, NotificationService notificationService) {
        this.minioService = minioService;
        this.executorService = executorService;
        this.notificationService = notificationService;
    }

    @Async
    public CompletableFuture<String> uploadAndConvertFile(UUID mediaId, MultipartFile mediaFile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Path inputTempFolder = Files.createTempDirectory("UPLOAD_MAIN_FILE_" + mediaId.toString());
                Path outputTempFolder = Files.createTempDirectory("CONVERTED_FILE_" + mediaId);

                NotificationResponse notificationResponse = NotificationResponse.builder().movieId(mediaId).status(StatusEnum.UPLOADING).progress(100).build();
                this.notificationService.sendMovieProgressNotification(notificationResponse);
                Path inputFile = inputTempFolder.resolve(mediaId.toString());
                mediaFile.transferTo(inputFile);

                String masterFile = this.convertMediaFile(mediaId, inputFile, outputTempFolder);
                Files.deleteIfExists(inputFile);
                Files.deleteIfExists(inputTempFolder);
                Files.deleteIfExists(outputTempFolder);

                notificationResponse.setStatus(StatusEnum.COMPLETED);
                this.notificationService.sendMovieProgressNotification(notificationResponse);
                return masterFile;
            } catch (Exception e) {
                throw new RuntimeException("File upload and conversion failed", e);
            }
        });
    }

    private String convertMediaFile(UUID mediaId, Path inputFile, Path outputTempFolder) throws Exception {
        double totalDuration = this.getMediaDuration(inputFile.toAbsolutePath().normalize());
        Process process = new ProcessBuilder(FFmpegBuilder.buildConvertingCommand(
                inputFile.toAbsolutePath().normalize(),
                outputTempFolder, mediaId)
        ).redirectErrorStream(true).start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            Pattern pattern = Pattern.compile("out_time=([0-9:.]+)");
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    this.notificationService.sendMovieProgressNotification(NotificationResponse.builder().movieId(mediaId).status(StatusEnum.CONVERTING).progress(ProgressBuilder.calculateProgress(matcher, totalDuration)).build());
                }
            }
        }

        int exitCode = process.waitFor();
        String masterFile;
        if (exitCode == 0) masterFile = FFmpegBuilder.buildMasterFile(outputTempFolder, mediaId);
        else {
            this.taskKill();
            throw new BadRequestException("FFmpeg process converting failed with exit code: " + exitCode);
        }
        this.saveSegments(mediaId, outputTempFolder);
        return masterFile;
    }

    private void saveSegments(UUID mediaId, Path outputTempFolder) throws IOException, BadRequestException {
        try (Stream<Path> segmentPathsStream = Files.list(outputTempFolder).filter(Files::isRegularFile)) {
            List<Path> segmentPaths = segmentPathsStream.toList();
            double totalSegments = segmentPaths.size();
            double progressPerSegment = 100 / totalSegments;
            double cumulativeProgress = 0;

            for (Path path : segmentPaths) {
                String fileName = path.getFileName().toString();
                String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
                String contentType = (fileExtension.equals("m3u8") ? "application/vnd.apple.mpegurl" : "video/mp2t");

                try (InputStream inputStream = new FileInputStream(path.toFile())) {
                    this.minioService.saveObject(mediaId, fileName, inputStream, contentType);
                    cumulativeProgress += progressPerSegment;
                    this.notificationService.sendMovieProgressNotification(NotificationResponse.builder().movieId(mediaId).status(StatusEnum.SAVING).progress((int) cumulativeProgress).build());
                    System.out.println(cumulativeProgress);
                } catch (Exception e) {
                    throw new BadRequestException("Can't upload :" + e.getMessage());
                } finally {
                    Files.deleteIfExists(path);
                }
            }
        }
    }

    private double getMediaDuration(Path inputPath) throws Exception {
        Process ffprobeProcess = new ProcessBuilder(FFmpegBuilder.buildDurationCommand(inputPath)).start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ffprobeProcess.getInputStream()))) {
            String durationLine = reader.readLine();
            return Double.parseDouble(durationLine);
        }
    }

    private void taskKill() throws InterruptedException, IOException {
        Process processKill = new ProcessBuilder(FFmpegBuilder.buildTaskkillCommand()).start();
        processKill.waitFor();
    }

    @PreDestroy
    public void shutdown() throws InterruptedException, IOException {
        executorService.shutdown();
        this.taskKill();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
