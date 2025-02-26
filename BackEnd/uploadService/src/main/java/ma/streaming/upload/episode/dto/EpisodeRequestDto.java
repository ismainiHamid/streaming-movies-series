package ma.streaming.upload.episode.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.series.dto.SeriesRequestDto;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EpisodeRequestDto extends SeriesRequestDto {
    private String runtime;
    private String director;
}
