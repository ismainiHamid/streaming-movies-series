package ma.streaming.upload.episode.mapper;

import ma.streaming.upload.episode.EpisodeEntity;
import ma.streaming.upload.episode.dto.EpisodeRequestDto;
import ma.streaming.upload.episode.dto.EpisodeResponseDto;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface EpisodeMapper {
    EpisodeEntity toEntity(EpisodeRequestDto episodeRequestDto) throws IOException;

    EpisodeResponseDto toResponseDto(EpisodeEntity episodeEntity);

    List<EpisodeResponseDto> toResponseDtoList(List<EpisodeEntity> episodeEntities);
}
