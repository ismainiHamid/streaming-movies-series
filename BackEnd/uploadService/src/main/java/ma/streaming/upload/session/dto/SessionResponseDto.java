package ma.streaming.upload.session.dto;

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
public class SessionResponseDto {
    private UUID id;
    private String email;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
}
