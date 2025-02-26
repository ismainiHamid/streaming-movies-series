package ma.streaming.upload.actor.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ActorResponseDto {
    private UUID id;
    private String name;
    private LocalDateTime createdAt;
}
