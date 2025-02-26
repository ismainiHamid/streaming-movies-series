package ma.streaming.upload.session;

import ma.streaming.upload.session.dto.SessionRequestDto;
import ma.streaming.upload.session.dto.SessionResponseDto;
import ma.streaming.upload.session.mapper.SessionMapper;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessionServiceImpl implements SessionService {
    private final SessionJpaRepository sessionJpaRepository;
    private final SessionMapper sessionMapper;

    public SessionServiceImpl(SessionJpaRepository sessionJpaRepository, SessionMapper sessionMapper) {
        this.sessionJpaRepository = sessionJpaRepository;
        this.sessionMapper = sessionMapper;
    }

    @Override
    public SessionResponseDto createSession(SessionRequestDto sessionRequestDto) {
        SessionEntity sessionEntity = this.sessionMapper.toSessionEntity(sessionRequestDto);
        sessionEntity.setExpiredAt(LocalDateTime.now().plusDays(8));
        return this.sessionMapper.toResponseDto(
                this.sessionJpaRepository.saveAndFlush(sessionEntity)
        );
    }

    @Override
    public SessionResponseDto deleteSession(String token) {
        return this.sessionMapper.toResponseDto(
                this.sessionJpaRepository.deleteByToken(token)
        );
    }

    @Override
    public boolean validateSession(String token) throws NotFoundException {
        SessionEntity sessionEntity = this.sessionJpaRepository.findByToken(token).orElseThrow(() ->
                new NotFoundException("The session with token:" + token + ", dose not exist.")
        );
        return sessionEntity.getExpiredAt().isAfter(LocalDateTime.now());
    }
}
