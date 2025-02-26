package ma.streaming.upload.subtitle.mapper;

import ma.streaming.upload.subtitle.SubtitleEntity;
import ma.streaming.upload.subtitle.dto.SubtitleRequestDto;
import ma.streaming.upload.subtitle.dto.SubtitleResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubtitleMapperImpl implements SubtitleMapper {

    @Override
    public SubtitleEntity toSubtitleEntity(SubtitleRequestDto subtitleRequestDto) {
        return SubtitleEntity.builder()
                .name(subtitleRequestDto.getName())
                .build();
    }

    @Override
    public SubtitleResponseDto toSubtitleResponseDto(SubtitleEntity subtitleEntity) {
        return SubtitleResponseDto.builder()
                .id(subtitleEntity.getId())
                .name(subtitleEntity.getName())
                .path(subtitleEntity.getPath())
                .createdAt(subtitleEntity.getCreatedAt())
                .build();
    }

    @Override
    public List<SubtitleResponseDto> toSubtitleResponseDtoList(List<SubtitleEntity> subtitleEntities) {
        return subtitleEntities.stream()
                .map(this::toSubtitleResponseDto)
                .toList();
    }
}
