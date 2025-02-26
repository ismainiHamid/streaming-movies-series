package ma.streaming.upload.movie.mapper;

import ma.streaming.upload.movie.MovieEntity;
import ma.streaming.upload.movie.dto.MovieRequestDto;
import ma.streaming.upload.movie.dto.MovieResponseDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class MovieMapperImpl implements MovieMapper {
    @Override
    public MovieEntity toMovieEntity(MovieRequestDto movieRequestDto) throws IOException {
        return MovieEntity.builder()
                .title(movieRequestDto.getTitle())
                .released(movieRequestDto.getReleased())
                .runtime(movieRequestDto.getRuntime())
                .director(movieRequestDto.getDirector())
                .plot(movieRequestDto.getPlot())
                .language(movieRequestDto.getLanguage())
                .poster(movieRequestDto.getPoster().getBytes())
                .thumbnail(movieRequestDto.getThumbnail().getBytes())
                .imdbRating(movieRequestDto.getImdbRating())
                .build();
    }

    @Override
    public MovieResponseDto toMovieResponseDto(MovieEntity movieEntity) {
        return MovieResponseDto.builder()
                .id(movieEntity.getId())
                .title(movieEntity.getTitle())
                .released(movieEntity.getReleased())
                .runtime(movieEntity.getRuntime())
                .director(movieEntity.getDirector())
                .plot(movieEntity.getPlot())
                .language(movieEntity.getLanguage())
                .imdbRating(movieEntity.getImdbRating())
                .masterFile(movieEntity.getMasterFile())
                .status(movieEntity.getStatus())
                .createdAt(movieEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<MovieResponseDto> toMovieResponseDtoList(List<MovieEntity> movieEntities) {
        return movieEntities.stream()
                .map(this::toMovieResponseDto)
                .toList();
    }
}
