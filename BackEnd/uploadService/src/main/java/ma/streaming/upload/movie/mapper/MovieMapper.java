package ma.streaming.upload.movie.mapper;

import ma.streaming.upload.movie.MovieEntity;
import ma.streaming.upload.movie.dto.MovieRequestDto;
import ma.streaming.upload.movie.dto.MovieResponseDto;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface MovieMapper {
    MovieEntity toMovieEntity(MovieRequestDto movieRequestDto) throws IOException;

    MovieResponseDto toMovieResponseDto(MovieEntity movieEntity);

    List<MovieResponseDto> toMovieResponseDtoList(List<MovieEntity> movieEntityList);
}
