package ma.streaming.upload.episode.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.series.dto.SeriesResponseDto;
import ma.streaming.upload.shared.enums.StatusEnum;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EpisodeResponseDto extends SeriesResponseDto {
    private String runtime;
    private String director;
    private String masterFile;
    private StatusEnum status;
}
