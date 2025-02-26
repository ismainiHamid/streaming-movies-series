package ma.streaming.upload.actor.mapper;

import ma.streaming.upload.actor.ActorEntity;
import ma.streaming.upload.actor.dto.ActorRequestDto;
import ma.streaming.upload.actor.dto.ActorResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorMapper {
    ActorEntity toActorEntity(ActorRequestDto actorRequestDto);

    ActorResponseDto toActorResponseDto(ActorEntity actorEntity);

    List<ActorResponseDto> toActorResponseDtoList(List<ActorEntity> actorEntities);
}
