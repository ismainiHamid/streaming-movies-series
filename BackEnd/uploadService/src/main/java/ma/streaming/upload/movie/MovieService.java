package ma.streaming.upload.movie;

import ma.streaming.upload.movie.dto.MovieRequestDto;
import ma.streaming.upload.movie.dto.MovieResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public interface MovieService {
    MovieResponseDto createMovie(MovieRequestDto movieRequestDto) throws AlreadyExistsException, IOException;

    MovieResponseDto modifyMovie(UUID movieId, MovieRequestDto movieRequestDto) throws AlreadyExistsException, NotFoundException, IOException;

    MovieResponseDto deleteMovie(UUID movieId) throws NotFoundException;

    Page<MovieResponseDto> findPaginatedMovies(int page, int size, String[] sort);

    MovieResponseDto findMovieById(UUID movieId) throws NotFoundException;

    Page<MovieResponseDto> findPaginatedMoviesByTitle(int page, int size, String[] sort, String title);

    MovieResponseDto uploadMovieFile(UUID movieId, MultipartFile movieFile) throws ExecutionException, InterruptedException;
}
