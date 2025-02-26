package ma.streaming.upload.genre;

import ma.streaming.upload.genre.dto.GenreRequestDto;
import ma.streaming.upload.genre.dto.GenreResponseDto;
import ma.streaming.upload.genre.mapper.GenreMapper;
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
public class GenreServiceImpl implements GenreService {
    private final GenreJpaRepository genreJpaRepository;
    private final GenreMapper genreMapper;

    public GenreServiceImpl(GenreJpaRepository genreJpaRepository, GenreMapper genreMapper) {
        this.genreJpaRepository = genreJpaRepository;
        this.genreMapper = genreMapper;
    }

    @Override
    @Transactional
    public GenreResponseDto createGenre(GenreRequestDto genreRequestDto) throws AlreadyExistsException {
        GenreEntity genreEntity = this.genreMapper.toGenreEntity(genreRequestDto);
        if (this.genreJpaRepository.existsByNameIgnoreCase(genreEntity.getName()))
            throw new AlreadyExistsException("The genre whit name: " + genreEntity.getName() + ", already exist.");
        return this.genreMapper.toGenreResponseDto(
                this.genreJpaRepository.saveAndFlush(genreEntity)
        );
    }

    @Override
    @Transactional
    public GenreResponseDto modifyGenre(UUID genreId, GenreRequestDto genreRequestDto) throws AlreadyExistsException, NotFoundException {
        GenreEntity genreEdited = this.genreMapper.toGenreEntity(genreRequestDto);
        GenreEntity genreEntity = this.genreJpaRepository.findById(genreId).orElseThrow(() ->
                new NotFoundException("Can't modify genre with id: " + genreId + ", dose not exist.")
        );

        if (!genreEdited.getName().equals(genreEntity.getName()))
            if (this.genreJpaRepository.existsByNameIgnoreCase(genreEdited.getName()))
                throw new AlreadyExistsException("Can't modify genre with name: " + genreEdited.getName() + ", already exist.");

        genreEntity.setName(genreEdited.getName());
        genreEntity.setUpdatedAt(LocalDateTime.now());
        return this.genreMapper.toGenreResponseDto(genreEntity);
    }

    @Override
    @Transactional
    public GenreResponseDto deleteGenre(UUID genreId) throws NotFoundException, BadRequestException {
        GenreEntity genreEntity = this.genreJpaRepository.findById(genreId).orElseThrow(() ->
                new NotFoundException("Can't delete genre with id: " + genreId + ", dose not exist.")
        );
        if (!genreEntity.getMovies().isEmpty())
            throw new BadRequestException("Can't delete genre with name: " + genreEntity.getName() + ", because related with movies.");
        if (!genreEntity.getSeries().isEmpty())
            throw new BadRequestException("Can't delete genre with name: " + genreEntity.getName() + ", because related with series.");
        if (!genreEntity.getEpisodes().isEmpty())
            throw new BadRequestException("Can't delete genre with name: " + genreEntity.getName() + ", because related with episodes.");
        this.genreJpaRepository.delete(genreEntity);
        return this.genreMapper.toGenreResponseDto(genreEntity);
    }

    @Override
    public Page<GenreResponseDto> findPaginatedGenres(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));

        return new PageImpl<>(this.genreMapper.toGenreResponseDtoList(
                this.genreJpaRepository.findAllBy(pageable)
        ), pageable, this.genreJpaRepository.count());
    }

    @Override
    public List<GenreResponseDto> findAllGenres() {
        return this.genreMapper.toGenreResponseDtoList(
                this.genreJpaRepository.findAllByOrderByNameAsc()
        );
    }

    @Override
    public GenreResponseDto findGenreById(UUID genreId) throws NotFoundException {
        return this.genreMapper.toGenreResponseDto(this.genreJpaRepository.findById(genreId).orElseThrow(() ->
                new NotFoundException("Can't find genre with id: " + genreId)
        ));
    }

    @Override
    public Page<GenreResponseDto> findPaginatedGenreByName(int page, int size, String[] sort, String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));
        return new PageImpl<>(this.genreMapper.toGenreResponseDtoList(
                this.genreJpaRepository.findAllByNameContainingIgnoreCase(name, pageable)
        ), pageable, this.genreJpaRepository.countByNameContainingIgnoreCase(name));
    }

    @Override
    public boolean existsGenreByName(String name) {
        return this.genreJpaRepository.existsByNameIgnoreCase(name);
    }
}
