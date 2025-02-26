package ma.streaming.upload.season.mapper;

import ma.streaming.upload.season.SeasonEntity;
import ma.streaming.upload.season.dto.SeasonRequestDto;
import ma.streaming.upload.season.dto.SeasonResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SeasonMapperImpl implements SeasonMapper {

    @Override
    public SeasonEntity toSeasonEntity(SeasonRequestDto seasonRequestDto) {
        return SeasonEntity.builder()
                .title(seasonRequestDto.getTitle())
                .number(seasonRequestDto.getNumber())
                .build();
    }

    @Override
    public SeasonResponseDto toSeasonResponseDto(SeasonEntity seasonEntity) {
        return SeasonResponseDto.builder()
                .id(seasonEntity.getId())
                .title(seasonEntity.getTitle())
                .number(seasonEntity.getNumber())
                .createAt(seasonEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<SeasonResponseDto> toSeasonResponseDtoList(List<SeasonEntity> seasonEntities) {
        return seasonEntities.stream()
                .map(this::toSeasonResponseDto)
                .toList();
    }
}
