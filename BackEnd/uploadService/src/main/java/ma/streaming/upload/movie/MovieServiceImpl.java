package ma.streaming.upload.movie;

import ma.streaming.upload.movie.dto.MovieRequestDto;
import ma.streaming.upload.movie.dto.MovieResponseDto;
import ma.streaming.upload.movie.mapper.MovieMapper;
import ma.streaming.upload.shared.enums.StatusEnum;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import ma.streaming.upload.shared.utils.ConvertingUtil;
import ma.streaming.upload.shared.utils.ImageUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class MovieServiceImpl implements MovieService {
    private final MovieJpaRepository movieJpaRepository;
    private final MovieMapper movieMapper;
    private final ConvertingUtil convertingUtil;
    private final ImageUtil imageUtil;

    public MovieServiceImpl(MovieJpaRepository movieJpaRepository, MovieMapper movieMapper, ConvertingUtil convertingUtil, ImageUtil imageUtil) {
        this.movieJpaRepository = movieJpaRepository;
        this.movieMapper = movieMapper;
        this.convertingUtil = convertingUtil;
        this.imageUtil = imageUtil;
    }

    @Override
    @Transactional
    public MovieResponseDto createMovie(MovieRequestDto movieRequestDto) throws AlreadyExistsException, IOException {
        MovieEntity movieEntity = this.movieMapper.toMovieEntity(movieRequestDto);
        if (this.movieJpaRepository.existsByTitleIgnoreCaseAndDeletedAtIsNull(movieEntity.getTitle()))
            throw new AlreadyExistsException("The movie with title: " + movieEntity.getTitle() + ", already exist.");
        else if (this.movieJpaRepository.existsByTitleIgnoreCase(movieEntity.getTitle()))
            throw new AlreadyExistsException("The movie with title: " + movieEntity.getTitle() + ", already exist in deleted movie.");

        movieEntity.setPoster(this.imageUtil.processImage(movieEntity.getPoster(),
                300,
                444,
                .85F
        ));

        movieEntity.setThumbnail(this.imageUtil.processImage(movieEntity.getThumbnail(),
                1920,
                1080,
                .85F
        ));

        return this.movieMapper.toMovieResponseDto(
                this.movieJpaRepository.saveAndFlush(movieEntity)
        );
    }

    @Override
    @Transactional
    public MovieResponseDto modifyMovie(UUID movieId, MovieRequestDto movieRequestDto) throws AlreadyExistsException, NotFoundException, IOException {
        MovieEntity movieEdited = this.movieMapper.toMovieEntity(movieRequestDto);
        MovieEntity movieEntity = this.movieJpaRepository.findByIdAndDeletedAtIsNull(movieId).orElseThrow(() ->
                new NotFoundException("Can't modify the movie with id:" + movieId + ", because dose not exist.")
        );

        if (!movieEntity.getTitle().equals(movieEdited.getTitle()))
            if (this.movieJpaRepository.existsByTitleIgnoreCaseAndDeletedAtIsNull(movieEdited.getTitle()))
                throw new AlreadyExistsException("Can't modify the movie with title: " + movieEdited.getTitle() + ", because already exist.");
            else if (this.movieJpaRepository.existsByTitleIgnoreCase(movieEntity.getTitle()))
                throw new AlreadyExistsException("The movie with title: " + movieEntity.getTitle() + ", already exist in deleted movie.");

        movieEntity.setTitle(movieEdited.getTitle());
        movieEntity.setReleased(movieEdited.getReleased());
        movieEntity.setRuntime(movieEdited.getRuntime());
        movieEntity.setDirector(movieEdited.getDirector());
        movieEntity.setPlot(movieEdited.getPlot());
        movieEntity.setLanguage(movieEdited.getLanguage());
        if (Objects.nonNull(movieEdited.getPoster()))
            movieEntity.setPoster(this.imageUtil.processImage(movieEdited.getPoster(),
                    300,
                    444,
                    .85F
            ));
        if (Objects.nonNull(movieEdited.getThumbnail()))
            movieEntity.setThumbnail(this.imageUtil.processImage(movieEntity.getThumbnail(),
                    1920,
                    1080,
                    .85F
            ));
        movieEntity.setUpdatedAt(LocalDateTime.now());
        return this.movieMapper.toMovieResponseDto(movieEntity);
    }

    @Override
    @Transactional
    public MovieResponseDto deleteMovie(UUID movieId) throws NotFoundException {
        MovieEntity movieEntity = this.movieJpaRepository.findByIdAndDeletedAtIsNull(movieId).orElseThrow(() ->
                new NotFoundException("The movie with id:" + movieId + ", can't deleted because dose not exist.")
        );
        movieEntity.setDeletedAt(LocalDateTime.now());
        return this.movieMapper.toMovieResponseDto(movieEntity);
    }

    @Override
    public Page<MovieResponseDto> findPaginatedMovies(int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));
        return new PageImpl<>(this.movieMapper.toMovieResponseDtoList(
                this.movieJpaRepository.findAllByDeletedAtIsNull(pageable)
        ), pageable, this.movieJpaRepository.countAllByDeletedAtIsNull());
    }

    @Override
    public MovieResponseDto findMovieById(UUID movieId) throws NotFoundException {
        return this.movieMapper.toMovieResponseDto(
                this.movieJpaRepository.findByIdAndDeletedAtIsNull(movieId).orElseThrow(() ->
                        new NotFoundException("The movie with id:" + movieId + ", dose not exist.")
                )
        );
    }

    @Override
    public Page<MovieResponseDto> findPaginatedMoviesByTitle(int page, int size, String[] sort, String title) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(
                Sort.Direction.fromString(sort[1]), sort[0]
        ));
        return new PageImpl<>(this.movieMapper.toMovieResponseDtoList(
                this.movieJpaRepository.findByDeletedAtIsNullAndTitleContainingIgnoreCase(title, pageable)
        ), pageable, this.movieJpaRepository.countByDeletedAtIsNullAndTitleContainingIgnoreCase(title));
    }

    @Override
    @Transactional
    public MovieResponseDto uploadMovieFile(UUID movieId, MultipartFile movieFile) throws ExecutionException, InterruptedException {
        MovieEntity movieEntity = this.movieJpaRepository.findByIdAndDeletedAtIsNull(movieId).orElseThrow(() ->
                new NotFoundException("Can't upload movie whit id:" + movieId + ", because dose not exist.")
        );
        String masterFile = this.convertingUtil.uploadAndConvertFile(movieEntity.getId(), movieFile).get();
        movieEntity.setMasterFile(masterFile);
        movieEntity.setStatus(StatusEnum.COMPLETED);
        return this.movieMapper.toMovieResponseDto(movieEntity);
    }
}
