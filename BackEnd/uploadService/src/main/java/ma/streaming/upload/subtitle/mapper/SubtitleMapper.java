package ma.streaming.upload.subtitle.mapper;

import ma.streaming.upload.subtitle.SubtitleEntity;
import ma.streaming.upload.subtitle.dto.SubtitleRequestDto;
import ma.streaming.upload.subtitle.dto.SubtitleResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtitleMapper {
    SubtitleEntity toSubtitleEntity(SubtitleRequestDto subtitleRequestDto);

    SubtitleResponseDto toSubtitleResponseDto(SubtitleEntity subtitleEntity);

    List<SubtitleResponseDto> toSubtitleResponseDtoList(List<SubtitleEntity> subtitleEntities);
}
