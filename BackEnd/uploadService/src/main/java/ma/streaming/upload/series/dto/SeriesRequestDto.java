package ma.streaming.upload.series.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.media.dto.MediaRequestDto;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SeriesRequestDto extends MediaRequestDto {
}
