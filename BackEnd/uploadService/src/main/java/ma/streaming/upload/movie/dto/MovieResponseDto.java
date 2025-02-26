package ma.streaming.upload.movie.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.media.dto.MediaResponseDto;
import ma.streaming.upload.shared.enums.StatusEnum;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MovieResponseDto extends MediaResponseDto {
    private String runtime;
    private String director;
    private String masterFile;
    private StatusEnum status;
}
