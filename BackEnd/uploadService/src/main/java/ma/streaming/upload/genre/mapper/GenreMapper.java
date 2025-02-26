package ma.streaming.upload.genre.mapper;

import ma.streaming.upload.genre.GenreEntity;
import ma.streaming.upload.genre.dto.GenreRequestDto;
import ma.streaming.upload.genre.dto.GenreResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreMapper {
    GenreEntity toGenreEntity(GenreRequestDto genreRequestDto);

    GenreResponseDto toGenreResponseDto(GenreEntity genreEntity);

    List<GenreResponseDto> toGenreResponseDtoList(List<GenreEntity> genreEntities);
}
