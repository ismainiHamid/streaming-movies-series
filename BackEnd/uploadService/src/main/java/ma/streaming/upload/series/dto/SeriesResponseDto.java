package ma.streaming.upload.series.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.streaming.upload.media.dto.MediaResponseDto;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SeriesResponseDto extends MediaResponseDto {
}
