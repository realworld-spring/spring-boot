package real.world.error.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import real.world.error.ErrorCode;

@Getter
public class AuthenticationErrorCodeException extends AuthenticationException {

    private ErrorCode errorCode;

    public AuthenticationErrorCodeException(String msg) {
        super(msg);
    }

    public static AuthenticationErrorCodeException of(ErrorCode errorCode) {
        AuthenticationErrorCodeException exception = new AuthenticationErrorCodeException(
            errorCode.getBody() + errorCode.getMessage());
        exception.errorCode = errorCode;
        return exception;
    }

}