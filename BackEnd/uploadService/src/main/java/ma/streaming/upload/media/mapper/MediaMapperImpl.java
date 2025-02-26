package ma.streaming.upload.media.mapper;

import ma.streaming.upload.media.MediaEntity;
import ma.streaming.upload.media.dto.MediaResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaMapperImpl implements MediaMapper {
    @Override
    public MediaResponseDto toMediaResponseDto(MediaEntity mediaEntity) {
        return MediaResponseDto.builder()
                .id(mediaEntity.getId())
                .title(mediaEntity.getTitle())
                .released(mediaEntity.getReleased())
                .plot(mediaEntity.getPlot())
                .language(mediaEntity.getLanguage())
                .imdbRating(mediaEntity.getImdbRating())
                .createdAt(mediaEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<MediaResponseDto> toMediaResponseDtoList(List<MediaEntity> mediaEntities) {
        return mediaEntities.stream()
                .map(this::toMediaResponseDto).toList();
    }
}
