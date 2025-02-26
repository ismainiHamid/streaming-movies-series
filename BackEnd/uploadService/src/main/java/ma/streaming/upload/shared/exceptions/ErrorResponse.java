package ma.streaming.upload.shared.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private int code;
    private String status;
    private String message;
    private LocalDateTime timestamp;
}
