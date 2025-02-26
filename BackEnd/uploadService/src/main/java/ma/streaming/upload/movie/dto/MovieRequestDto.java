package ma.streaming.upload.movie.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.media.dto.MediaRequestDto;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MovieRequestDto extends MediaRequestDto {
    private String runtime;
    private String director;
}
