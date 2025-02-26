package ma.streaming.upload.actor;

import ma.streaming.upload.actor.dto.ActorRequestDto;
import ma.streaming.upload.actor.dto.ActorResponseDto;
import ma.streaming.upload.actor.mapper.ActorMapper;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ActorServiceImpl implements ActorService {
    private final ActorJpaRepository actorJpaRepository;
    private final ActorMapper actorMapper;

    public ActorServiceImpl(ActorJpaRepository actorJpaRepository, ActorMapper actorMapper) {
        this.actorJpaRepository = actorJpaRepository;
        this.actorMapper = actorMapper;
    }

    @Override
    @Transactional
    public ActorResponseDto createdActor(ActorRequestDto actorRequestDto) throws AlreadyExistsException {
        ActorEntity actorEntity = this.actorMapper.toActorEntity(actorRequestDto);
        if (this.actorJpaRepository.existsByNameIgnoreCase(actorEntity.getName()))
            throw new AlreadyExistsException("The actor with name:" + actorEntity.getName() + ", already exist.");
        return this.actorMapper.toActorResponseDto(
                this.actorJpaRepository.saveAndFlush(actorEntity)
        );
    }

    @Override
    @Transactional
    public ActorResponseDto modifyActor(UUID actorId, ActorRequestDto actorRequestDto) throws AlreadyExistsException, NotFoundException {
        ActorEntity actorEdited = this.actorMapper.toActorEntity(actorRequestDto);
        ActorEntity actorEntity = this.actorJpaRepository.findById(actorId).orElseThrow(() ->
                new NotFoundException("Can't modify actor with id:" + actorId + " does not exist.")
        );

        if (!actorEdited.getName().equals(actorEntity.getName()))
            if (!this.actorJpaRepository.existsByNameIgnoreCase(actorEdited.getName()))
                throw new AlreadyExistsException("Can't modify actor with name:" + actorEdited.getName() + ", already exist.");

        actorEntity.setName(actorEdited.getName());
        actorEntity.setUpdatedAt(LocalDateTime.now());
        return this.actorMapper.toActorResponseDto(actorEntity);
    }

    @Override
    @Transactional
    public ActorResponseDto deleteActor(UUID actorId) throws NotFoundException, BadRequestException {
        ActorEntity actorEntity = this.actorJpaRepository.findById(actorId).orElseThrow(() ->
                new NotFoundException("Can't delete actor with id:" + actorId + " does not exist.")
        );
        if (!actorEntity.getMovies().isEmpty())
            throw new BadRequestException("Can't delete actor with name: " + actorEntity.getName() + ", because related with movies.");
        if (!actorEntity.getSeries().isEmpty())
            throw new BadRequestException("Can't delete actor with name: " + actorEntity.getName() + ", because related with series.");
        if (!actorEntity.getEpisodes().isEmpty())
            throw new BadRequestException("Can't delete actor with name: " + actorEntity.getName() + ", because related with episodes.");
        this.actorJpaRepository.delete(actorEntity);
        return this.actorMapper.toActorResponseDto(actorEntity);
    }

    @Override
    public Page<ActorResponseDto> findPaginatedActors(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));
        return new PageImpl<>(this.actorMapper.toActorResponseDtoList(
                this.actorJpaRepository.findAllBy(pageable)
        ), pageable, this.actorJpaRepository.count());
    }

    @Override
    public List<ActorResponseDto> findAllActors() {
        return this.actorMapper.toActorResponseDtoList(
                this.actorJpaRepository.findAllByOrderByNameAsc()
        );
    }

    @Override
    public ActorResponseDto findActorById(UUID actorId) throws NotFoundException {
        return this.actorMapper.toActorResponseDto(this.actorJpaRepository.findById(actorId).orElseThrow(() ->
                new NotFoundException("Can't modify actor with id:" + actorId + " does not exist.")
        ));
    }

    @Override
    public Page<ActorResponseDto> findPaginatedActorByName(int page, int size, String[] sort, String name) {
        Pageable pageable = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, "name")
        );
        return new PageImpl<>(this.actorMapper.toActorResponseDtoList(
                this.actorJpaRepository.findAllByNameContainingIgnoreCase(name, pageable)
        ), pageable, this.actorJpaRepository.countByNameContainingIgnoreCase(name));
    }

    @Override
    public boolean existsActorByName(String name) {
        return this.actorJpaRepository.existsByNameIgnoreCase(name);
    }
}
