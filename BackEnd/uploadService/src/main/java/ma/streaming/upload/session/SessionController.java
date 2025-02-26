package ma.streaming.upload.session;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import ma.streaming.upload.session.dto.SessionRequestDto;
import ma.streaming.upload.session.dto.SessionResponseDto;
import ma.streaming.upload.shared.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "http://localhost:4200")
@RequestMapping(path = "/v1/sessions")
@Tag(name = "Session", description = "Endpoints for managing user sessions.")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    @Operation(summary = "Create a new session", description = "Creates a session for a user and returns the session details.")
    @ApiResponse(responseCode = "201", description = "Session created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<SessionResponseDto> createSession(@RequestBody SessionRequestDto sessionRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                this.sessionService.createSession(sessionRequestDto)
        );
    }

    @DeleteMapping(path = "/{token}")
    @Operation(summary = "Delete a session", description = "Deletes a session based on the token.")
    @ApiResponse(responseCode = "200", description = "Session deleted successfully")
    @ApiResponse(responseCode = "404", description = "Session not found")
    public ResponseEntity<SessionResponseDto> deleteSession(@PathVariable String token) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                this.sessionService.deleteSession(token)
        );
    }

    @GetMapping(path = "/{token}/validate")
    @Operation(summary = "Validate a session", description = "Checks if a session is still valid based on the token.")
    @ApiResponse(responseCode = "200", description = "Session is valid")
    @ApiResponse(responseCode = "404", description = "Session not found")
    public ResponseEntity<Boolean> validateSession(@PathVariable String token) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.sessionService.validateSession(token)
        );
    }
}
