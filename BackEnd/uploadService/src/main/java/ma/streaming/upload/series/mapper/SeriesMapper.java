package ma.streaming.upload.series.mapper;

import ma.streaming.upload.series.SeriesEntity;
import ma.streaming.upload.series.dto.SeriesRequestDto;
import ma.streaming.upload.series.dto.SeriesResponseDto;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface SeriesMapper {
    SeriesEntity toEntity(SeriesRequestDto seriesRequestDto) throws IOException;

    SeriesResponseDto toResponseDto(SeriesEntity seriesEntity);

    List<SeriesResponseDto> toResponseDtoList(List<SeriesEntity> seriesEntities);
}
