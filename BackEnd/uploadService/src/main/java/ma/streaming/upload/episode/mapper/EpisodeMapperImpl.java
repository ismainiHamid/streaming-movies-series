package ma.streaming.upload.episode.mapper;

import ma.streaming.upload.episode.EpisodeEntity;
import ma.streaming.upload.episode.dto.EpisodeRequestDto;
import ma.streaming.upload.episode.dto.EpisodeResponseDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EpisodeMapperImpl implements EpisodeMapper {

    @Override
    public EpisodeEntity toEntity(EpisodeRequestDto episodeRequestDto) throws IOException {
        return EpisodeEntity.builder()
                .title(episodeRequestDto.getTitle())
                .released(episodeRequestDto.getReleased())
                .runtime(episodeRequestDto.getRuntime())
                .director(episodeRequestDto.getDirector())
                .plot(episodeRequestDto.getPlot())
                .language(episodeRequestDto.getLanguage())
                .poster(episodeRequestDto.getPoster().getBytes())
                .imdbRating(episodeRequestDto.getImdbRating())
                .build();
    }

    @Override
    public EpisodeResponseDto toResponseDto(EpisodeEntity episodeEntity) {
        return EpisodeResponseDto.builder()
                .id(episodeEntity.getId())
                .title(episodeEntity.getTitle())
                .released(episodeEntity.getReleased())
                .runtime(episodeEntity.getRuntime())
                .director(episodeEntity.getDirector())
                .plot(episodeEntity.getPlot())
                .language(episodeEntity.getLanguage())
                .imdbRating(episodeEntity.getImdbRating())
                .masterFile(episodeEntity.getMasterFile())
                .status(episodeEntity.getStatus())
                .createdAt(episodeEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<EpisodeResponseDto> toResponseDtoList(List<EpisodeEntity> episodeEntities) {
        return episodeEntities.stream()
                .map(this::toResponseDto)
                .toList();
    }
}
