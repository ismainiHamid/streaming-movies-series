package ma.streaming.upload.media.mapper;

import ma.streaming.upload.media.MediaEntity;
import ma.streaming.upload.media.dto.MediaResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaMapper {
    MediaResponseDto toMediaResponseDto(MediaEntity mediaEntity);

    List<MediaResponseDto> toMediaResponseDtoList(List<MediaEntity> mediaEntities);
}
