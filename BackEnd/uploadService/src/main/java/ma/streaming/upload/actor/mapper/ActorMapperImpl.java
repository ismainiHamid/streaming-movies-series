package ma.streaming.upload.actor.mapper;

import ma.streaming.upload.actor.ActorEntity;
import ma.streaming.upload.actor.dto.ActorRequestDto;
import ma.streaming.upload.actor.dto.ActorResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActorMapperImpl implements ActorMapper {
    @Override
    public ActorEntity toActorEntity(ActorRequestDto actorRequestDto) {
        return ActorEntity.builder()
                .name(actorRequestDto.getName())
                .build();
    }

    @Override
    public ActorResponseDto toActorResponseDto(ActorEntity actorEntity) {
        return ActorResponseDto.builder()
                .id(actorEntity.getId())
                .name(actorEntity.getName())
                .createdAt(actorEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<ActorResponseDto> toActorResponseDtoList(List<ActorEntity> actorEntities) {
        return actorEntities.stream()
                .map(this::toActorResponseDto)
                .toList();
    }
}
