package ma.streaming.upload.shared.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
@ConfigurationProperties(prefix = "ffmpeg")
public class FFmpegConfig {
    private Map<String, String> commands = new LinkedHashMap<>();
    private Map<String, Integer> bandwidths = new LinkedHashMap<>();
    private Map<String, String> resolutions = new LinkedHashMap<>();
}
