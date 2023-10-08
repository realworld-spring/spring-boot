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
    USERNAME_NOT_EXIST(HttpStatus.UNPROCESSABLE_ENTITY, "userId", "not exists."),

    // Follow
    RECURSIVE_FOLLOW(HttpStatus.UNPROCESSABLE_ENTITY, "recursive follow", "cannot follow self"),
    ALREADY_FOLLOWING(HttpStatus.UNPROCESSABLE_ENTITY, "follow", "already follow."),
    FOLLOW_NOT_EXIST(HttpStatus.UNPROCESSABLE_ENTITY, "follow", "not exists."),

    // Article
    ARTICLE_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY, "article", "not found."),
    ARTICLE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "article", "you have no authority"),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY, "comment", "not found."),
    COMMENT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "comment", "you have no authority"),
  
    // Favorite
    ALREADY_FAVORITE(HttpStatus.UNPROCESSABLE_ENTITY, "favorite", "already exists."),
    FAVORITE_NOT_FOUND(HttpStatus.UNPROCESSABLE_ENTITY, "favorite", "not found."),

    // Authentication
    AUTH_FORMAT_INVALID(HttpStatus.UNPROCESSABLE_ENTITY, "authentication", "format invalid."),
    AUTH_USERNAME_NOT_EXIST(HttpStatus.UNPROCESSABLE_ENTITY, "authentication", "user is not exist."),
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
