package ma.streaming.upload.session.mapper;

import ma.streaming.upload.session.SessionEntity;
import ma.streaming.upload.session.dto.SessionRequestDto;
import ma.streaming.upload.session.dto.SessionResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionMapperImpl implements SessionMapper {
    @Override
    public SessionEntity toSessionEntity(SessionRequestDto sessionRequestDto) {
        return SessionEntity.builder()
                .email(sessionRequestDto.getEmail())
                .token(sessionRequestDto.getToken())
                .build();
    }

    @Override
    public SessionResponseDto toResponseDto(SessionEntity sessionEntity) {
        return SessionResponseDto.builder()
                .id(sessionEntity.getId())
                .email(sessionEntity.getEmail())
                .token(sessionEntity.getToken())
                .createdAt(sessionEntity.getCreatedAt())
                .expiredAt(sessionEntity.getExpiredAt())
                .build();
    }

    @Override
    public List<SessionResponseDto> toResponseDtoList(List<SessionEntity> sessionEntities) {
        return sessionEntities.stream()
                .map(this::toResponseDto)
                .toList();
    }
}
