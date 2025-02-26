package ma.streaming.upload.session;

import ma.streaming.upload.session.dto.SessionRequestDto;
import ma.streaming.upload.session.dto.SessionResponseDto;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionService {
    SessionResponseDto createSession(SessionRequestDto sessionRequestDto);

    SessionResponseDto deleteSession(String token);

    boolean validateSession(String token) throws NotFoundException;
}
