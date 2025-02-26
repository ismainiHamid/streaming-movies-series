package ma.streaming.upload.actor;

import ma.streaming.upload.actor.dto.ActorRequestDto;
import ma.streaming.upload.actor.dto.ActorResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActorService {
    ActorResponseDto createdActor(ActorRequestDto actorRequestDto) throws AlreadyExistsException;

    ActorResponseDto modifyActor(UUID actorId, ActorRequestDto actorRequestDto) throws AlreadyExistsException, NotFoundException;

    ActorResponseDto deleteActor(UUID actorId) throws NotFoundException, BadRequestException;

    Page<ActorResponseDto> findPaginatedActors(int page, int size, String[] sort);

    List<ActorResponseDto> findAllActors();

    ActorResponseDto findActorById(UUID actorId) throws NotFoundException;

    Page<ActorResponseDto> findPaginatedActorByName(int page, int size, String[] sort, String name);

    boolean existsActorByName(String name);
}
