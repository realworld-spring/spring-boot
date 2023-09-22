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
    USERNAME_ALREADY_EXIST(HttpStatus.UNPROCESSABLE_ENTITY, "username", "already exists."),
    USERID_NOT_EXIST(HttpStatus.UNPROCESSABLE_ENTITY, "userId", "not exists."),

    // Article
    ARTICLE_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY, "article", "not found."),

    // Authentication
    AUTH_FORMAT_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "authentication", "format invalid."),
    USERNAME_NOT_EXIST(HttpStatus.UNPROCESSABLE_ENTITY, "authentication", "user is not exist."),
    WRONG_PASSWORD(HttpStatus.UNPROCESSABLE_ENTITY, "authentication", "wrong password."),

    // JWT
    JWT_FORMAT_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "jwt", "invalid format."),
    JWT_SIGNATURE_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "jwt", "invalid signature."),
    JWT_UNSUPPORTED(HttpStatus.UNPROCESSABLE_ENTITY, "jwt", "unsupported format."),
    JWT_EXPIRED(HttpStatus.UNPROCESSABLE_ENTITY, "jwt", "expired."),
    JWT_CLAIMS_EMPTY(HttpStatus.UNPROCESSABLE_ENTITY, "jwt", "claims empty.");

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
