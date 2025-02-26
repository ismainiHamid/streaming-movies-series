package ma.streaming.upload.session.mapper;

import ma.streaming.upload.session.SessionEntity;
import ma.streaming.upload.session.dto.SessionRequestDto;
import ma.streaming.upload.session.dto.SessionResponseDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionMapper {
    SessionEntity toSessionEntity(SessionRequestDto sessionRequestDto);

    SessionResponseDto toResponseDto(SessionEntity sessionEntity);

    List<SessionResponseDto> toResponseDtoList(List<SessionEntity> sessionEntities);
}
