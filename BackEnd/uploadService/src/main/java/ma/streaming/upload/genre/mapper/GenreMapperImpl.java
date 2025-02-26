package ma.streaming.upload.genre.mapper;

import ma.streaming.upload.genre.GenreEntity;
import ma.streaming.upload.genre.dto.GenreRequestDto;
import ma.streaming.upload.genre.dto.GenreResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenreMapperImpl implements GenreMapper {
    @Override
    public GenreEntity toGenreEntity(GenreRequestDto genreRequestDto) {
        return GenreEntity.builder()
                .name(genreRequestDto.getName())
                .build();
    }

    @Override
    public GenreResponseDto toGenreResponseDto(GenreEntity genreEntity) {
        return GenreResponseDto.builder()
                .id(genreEntity.getId())
                .name(genreEntity.getName())
                .createdAt(genreEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<GenreResponseDto> toGenreResponseDtoList(List<GenreEntity> genreEntities) {
        return genreEntities.stream()
                .map(this::toGenreResponseDto)
                .toList();
    }
}
