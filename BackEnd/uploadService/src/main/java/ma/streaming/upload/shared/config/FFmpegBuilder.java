package ma.streaming.upload.shared.config;

import java.nio.file.Path;
import java.util.*;

public class FFmpegBuilder {
    private static final List<String> QUALITIES = new ArrayList<>();
    private static final Map<String, String> VIDEO_BITRATE = new LinkedHashMap<>();
    private static final Map<String, String> AUDIO_BITRATE = new LinkedHashMap<>();

    static {
        QUALITIES.add("144p");
        QUALITIES.add("240p");
        QUALITIES.add("360p");
        QUALITIES.add("480p");
        QUALITIES.add("720p");
        QUALITIES.add("1080p");
        QUALITIES.add("1440p");
        QUALITIES.add("2160p");

        VIDEO_BITRATE.put("144p", "300k");
        VIDEO_BITRATE.put("240p", "500k");
        VIDEO_BITRATE.put("360p", "800k");
        VIDEO_BITRATE.put("480p", "1000k");
        VIDEO_BITRATE.put("720p", "1500k");
        VIDEO_BITRATE.put("1080p", "3000k");
        VIDEO_BITRATE.put("1440p", "5000k");
        VIDEO_BITRATE.put("2160p", "8000k");

        AUDIO_BITRATE.put("144p", "64k");
        AUDIO_BITRATE.put("240p", "64k");
        AUDIO_BITRATE.put("360p", "96k");
        AUDIO_BITRATE.put("480p", "128k");
        AUDIO_BITRATE.put("720p", "192k");
        AUDIO_BITRATE.put("1080p", "192k");
        AUDIO_BITRATE.put("1440p", "256k");
        AUDIO_BITRATE.put("2160p", "320k");
    }

    public static List<String> buildCommand(Path inputPath, Path outputPath, UUID mediaId) {
        final List<String> command = new ArrayList<>();
        command.add("cmd.exe");
        command.add("/c");
        command.add("ffmpeg");
        command.add("-loglevel");
        command.add("debug");
        command.add("-i");
        command.add(inputPath.toString());
        command.add("-filter_complex");
        command.add("[0:v]split=8[v144p][v240p][v360p][v480p][v720p][v1080p][v1440p][v2160p];" +
                "[v144p]scale=256:144[v144p_out];" +
                "[v240p]scale=426:240[v240p_out];" +
                "[v360p]scale=640:360[v360p_out];" +
                "[v480p]scale=854:480[v480p_out];" +
                "[v720p]scale=1280:720[v720p_out];" +
                "[v1080p]scale=1920:1080[v1080p_out];" +
                "[v1440p]scale=2560:1440[v1440p_out];" +
                "[v2160p]scale=3840:2160[v2160p_out]");
        for (int i = 0; i < QUALITIES.size(); i++) {
            command.add("-map");
            command.add(String.format("[v%s_out]", QUALITIES.get(i)));
            command.add("-map");
            command.add("0:a:0");
            command.add(String.format("-c:v:%d", i));
            command.add("libx264");
            command.add(String.format("-b:v:%d", i));
            command.add(VIDEO_BITRATE.get(QUALITIES.get(i)));
            command.add(String.format("-c:a:%d", i));
            command.add("aac");
            command.add(String.format("-b:a:%d", i));
            command.add(AUDIO_BITRATE.get(QUALITIES.get(i)));
            command.add("-hls_time");
            command.add("10");
            command.add("-hls_playlist_type");
            command.add("vod");
            command.add("-hls_segment_filename");
            command.add(outputPath.toString() + "\\" + mediaId + "_" + QUALITIES.get(i) + "_%05d.ts");
            command.add(String.format("%s\\%s_%s.m3u8", outputPath, mediaId, QUALITIES.get(i)));
        }
        command.add("-y");
        command.add("-progress");
        command.add("-");
        return command;
    }
}
