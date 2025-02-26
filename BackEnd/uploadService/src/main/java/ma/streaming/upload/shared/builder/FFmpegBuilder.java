package ma.streaming.upload.shared.builder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FFmpegBuilder {
    private static final List<String> QUALITIES = new ArrayList<>();
    private static final Map<String, String> VIDEO_BITRATE = new LinkedHashMap<>();
    private static final Map<String, String> AUDIO_BITRATE = new LinkedHashMap<>();
    private static final Map<String, Integer> BANDWIDTHS = new LinkedHashMap<>();
    private static final Map<String, String> RESOLUTIONS = new LinkedHashMap<>();

    static {
        QUALITIES.addAll(List.of(
                "144p",
                "240p",
                "360p",
                "480p",
                "720p",
                "1080p",
                "1440p",
                "2160p"
        ));

        VIDEO_BITRATE.putAll(Map.of(
                "144p", "300k",
                "240p", "500k",
                "360p", "800k",
                "480p", "1000k",
                "720p", "1500k",
                "1080p", "3000k",
                "1440p", "5000k",
                "2160p", "8000k"
        ));

        AUDIO_BITRATE.putAll(Map.of(
                "144p", "64k",
                "240p", "64k",
                "360p", "96k",
                "480p", "128k",
                "720p", "192k",
                "1080p", "192k",
                "1440p", "256k",
                "2160p", "320k"
        ));

        BANDWIDTHS.putAll(Map.of(
                "144p", 150000,
                "240p", 300000,
                "360p", 500000,
                "480p", 1000000,
                "720p", 3000000,
                "1080p", 5000000,
                "1440p", 6000000,
                "2160p", 8000000
        ));

        RESOLUTIONS.putAll(Map.of(
                "144p", "256x144",
                "240p", "426x240",
                "360p", "640x360",
                "480p", "854x480",
                "720p", "1280x720",
                "1080p", "1920x1080",
                "1440p", "2560x1440",
                "2160p", "3840x2160"
        ));
    }

    public static List<String> buildConvertingCommand(Path inputPath, Path outputPath, UUID mediaId) {
        final List<String> command = new ArrayList<>();
        command.add("cmd.exe");
        command.add("/c");
        command.add("ffmpeg");
        command.add("-loglevel");
        command.add("info");
        command.add("-i");
        command.add(inputPath.toString());
        command.add("-filter_complex");
        command.add("[0:v]split=8[v144p][v240p][v360p][v480p][v720p][v1080p][v1440p][v2160p];" +
                "[v144p]scale=256:144:flags=lanczos[v144p_out];" +
                "[v240p]scale=426:240:flags=lanczos[v240p_out];" +
                "[v360p]scale=640:360:flags=lanczos[v360p_out];" +
                "[v480p]scale=854:480:flags=lanczos[v480p_out];" +
                "[v720p]scale=1280:720:flags=lanczos[v720p_out];" +
                "[v1080p]scale=1920:1080:flags=lanczos[v1080p_out];" +
                "[v1440p]scale=2560:1440:flags=lanczos[v1440p_out];" +
                "[v2160p]scale=3840:2160:flags=lanczos[v2160p_out];");
        for (int i = 0; i < QUALITIES.size(); i++) {
            command.add("-map");
            command.add(String.format("[v%s_out]", QUALITIES.get(i)));
            command.add("-map");
            command.add("0:a:0");
            command.add(String.format("-c:v:%d", i));
            command.add("libx264");
            command.add("-preset");
            command.add("fast");
            command.add("-crf");
            command.add("23");
            command.add(String.format("-b:v:%d", i));
            command.add(VIDEO_BITRATE.get(QUALITIES.get(i)));
            command.add(String.format("-c:a:%d", i));
            command.add("aac");
            command.add(String.format("-b:a:%d", i));
            command.add(AUDIO_BITRATE.get(QUALITIES.get(i)));
            command.add("-hls_time");
            command.add("5");
            command.add("-hls_playlist_type");
            command.add("vod");
            command.add("-hls_segment_filename");
            command.add("\"" + outputPath.toString() + "\\" + mediaId + "_" + QUALITIES.get(i) + "_%05d.ts\"");
            command.add("\"" + String.format("%s\\%s_%s.m3u8", outputPath, mediaId, QUALITIES.get(i)) + "\"");
        }
        command.add("-y");
        command.add("-progress");
        command.add("-");
        return command;
    }

    public static List<String> buildDurationCommand(Path inputPath) {
        return Arrays.asList(
                "cmd.exe",
                "/c",
                "ffprobe",
                "-i",
                inputPath.toString(),
                "-show_entries",
                "format=duration",
                "-v",
                "quiet",
                "-of",
                "csv=p=0"
        );
    }

    public static List<String> buildTaskkillCommand() {
        return Arrays.asList("cmd.exe", "/c", "taskkill", "/F", "/IM", "ffmpeg.exe");
    }

    public static String buildMasterFile(Path outputPath, UUID mediaId) throws IOException {
        Path masterFilePath = outputPath.resolve(String.format("%s_master.m3u8", mediaId));
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(masterFilePath)) {
            for (Map.Entry<String, Integer> bandwidth : BANDWIDTHS.entrySet()) {
                bufferedWriter.write("#EXTM3U\n");
                bufferedWriter.write(String.format("#EXT-X-STREAM-INF:BANDWIDTH=%s,RESOLUTION=%s\n", bandwidth.getValue(), RESOLUTIONS.get(bandwidth.getKey())));
                bufferedWriter.write(String.format("%s_%s.m3u8\n", mediaId, bandwidth.getKey()));
            }
        }
        return masterFilePath.toAbsolutePath().getFileName().normalize().toString();
    }

}
