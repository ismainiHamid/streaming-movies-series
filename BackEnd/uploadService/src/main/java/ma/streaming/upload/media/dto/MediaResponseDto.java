package ma.streaming.upload.media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponseDto {
    private UUID id;
    private String title;
    private String released;
    private String plot;
    private String language;
    private String imdbRating;
    private String type;
    private LocalDateTime createdAt;
}
