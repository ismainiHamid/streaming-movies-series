package ma.streaming.upload.media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MediaRequestDto {
    private String title;
    private String released;
    private String plot;
    private String language;
    private MultipartFile poster;
    private MultipartFile thumbnail;
    private String imdbRating;
}
