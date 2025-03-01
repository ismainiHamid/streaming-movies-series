package ma.streaming.upload.subtitle.dto;

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
public class SubtitleResponseDto {
    private UUID id;
    private String name;
    private String path;
    private LocalDateTime createdAt;
}
