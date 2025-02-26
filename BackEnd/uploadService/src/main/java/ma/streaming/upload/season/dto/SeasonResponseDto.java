package ma.streaming.upload.season.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonResponseDto {
    private UUID id;
    private String title;
    private Integer number;
    private LocalDateTime createAt;
}
