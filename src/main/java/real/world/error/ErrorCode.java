package real.world.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Global
    REQUEST_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "request", "invalid."),
    INTERNAL_SERVER(HttpStatus.UNPROCESSABLE_ENTITY, "internal", "server error."),


    // User
    USERNAME_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "username", "invalid."),
    EMAIL_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "email", "invalid."),
    USERNAME_ALREADY_EXIST(HttpStatus.UNPROCESSABLE_ENTITY, "username", "already exists.");

    private final HttpStatus code;

    private final String body;

    private final String message;

    ErrorCode(HttpStatus code, String body, String message) {
        this.code = code;
        this.body = body;
        this.message = message;
    }

    ErrorCode(HttpStatus code) {
        this(code, "", "");
    }

    ErrorCode(HttpStatus code, String message) {
        this(code, "", message);
    }

}
