package ma.streaming.upload.shared.builder;

import java.util.regex.Matcher;

public class ProgressBuilder {
    public static int calculateProgress(Matcher matcher, double totalDuration) {
        double currentTime = parseTime(matcher.group(1));
        return (int) ((currentTime / totalDuration) * 100);
    }

    private static double parseTime(String time) {
        String[] parts = time.split(":");
        double seconds = 0.0;
        if (parts.length == 3) {
            seconds += Double.parseDouble(parts[0]) * 3600;
            seconds += Double.parseDouble(parts[1]) * 60;
            seconds += Double.parseDouble(parts[2]);
        } else if (parts.length == 2) {
            seconds += Double.parseDouble(parts[0]) * 60;
            seconds += Double.parseDouble(parts[1]);
        } else seconds += Double.parseDouble(parts[0]);
        return seconds;
    }
}
