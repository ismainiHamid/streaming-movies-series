package ma.streaming.upload.actor.dto;

import lombok.*;

@Data
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ActorRequestDto {
    private String name;
}
