package ma.streaming.upload.season.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonRequestDto {
    private String title;
    private Integer number;
    private UUID seriesId;
}
