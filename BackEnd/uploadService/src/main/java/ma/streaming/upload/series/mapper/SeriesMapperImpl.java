package ma.streaming.upload.series.mapper;

import ma.streaming.upload.series.SeriesEntity;
import ma.streaming.upload.series.dto.SeriesRequestDto;
import ma.streaming.upload.series.dto.SeriesResponseDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SeriesMapperImpl implements SeriesMapper {
    @Override
    public SeriesEntity toEntity(SeriesRequestDto seriesRequestDto) throws IOException {
        return SeriesEntity.builder()
                .title(seriesRequestDto.getTitle())
                .released(seriesRequestDto.getReleased())
                .plot(seriesRequestDto.getPlot())
                .language(seriesRequestDto.getLanguage())
                .poster(seriesRequestDto.getPoster().getBytes())
                .imdbRating(seriesRequestDto.getImdbRating())
                .build();
    }

    @Override
    public SeriesResponseDto toResponseDto(SeriesEntity seriesEntity) {
        return SeriesResponseDto.builder()
                .id(seriesEntity.getId())
                .title(seriesEntity.getTitle())
                .released(seriesEntity.getReleased())
                .plot(seriesEntity.getPlot())
                .language(seriesEntity.getLanguage())
                .imdbRating(seriesEntity.getImdbRating())
                .createdAt(seriesEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<SeriesResponseDto> toResponseDtoList(List<SeriesEntity> seriesEntities) {
        return seriesEntities.stream()
                .map(this::toResponseDto)
                .toList();
    }
}
