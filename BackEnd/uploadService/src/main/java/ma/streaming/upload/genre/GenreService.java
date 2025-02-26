package ma.streaming.upload.genre;

import ma.streaming.upload.genre.dto.GenreRequestDto;
import ma.streaming.upload.genre.dto.GenreResponseDto;
import ma.streaming.upload.shared.exceptions.AlreadyExistsException;
import ma.streaming.upload.shared.exceptions.BadRequestException;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GenreService {
    GenreResponseDto createGenre(GenreRequestDto genreRequestDto) throws AlreadyExistsException;

    GenreResponseDto modifyGenre(UUID genreId, GenreRequestDto genreRequestDto) throws AlreadyExistsException, NotFoundException;

    GenreResponseDto deleteGenre(UUID genreId) throws NotFoundException, BadRequestException;

    Page<GenreResponseDto> findPaginatedGenres(int page, int size, String[] sort);

    List<GenreResponseDto> findAllGenres();

    GenreResponseDto findGenreById(UUID genreId) throws NotFoundException;

    Page<GenreResponseDto> findPaginatedGenreByName(int page, int size, String[] sort, String name);

    boolean existsGenreByName(String name);
}
