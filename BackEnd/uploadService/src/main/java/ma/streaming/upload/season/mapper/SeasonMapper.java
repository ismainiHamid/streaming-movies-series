package ma.streaming.upload.season.mapper;

import ma.streaming.upload.season.SeasonEntity;
import ma.streaming.upload.season.dto.SeasonRequestDto;
import ma.streaming.upload.season.dto.SeasonResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeasonMapper {
    SeasonEntity toSeasonEntity(SeasonRequestDto seasonRequestDto);

    SeasonResponseDto toSeasonResponseDto(SeasonEntity seasonEntity);

    List<SeasonResponseDto> toSeasonResponseDtoList(List<SeasonEntity> seasonEntities);
}
