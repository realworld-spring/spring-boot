package real.world.error.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import real.world.error.ErrorCode;

@Getter
public class AuthenticationErrorCodeException extends AuthenticationException {

    private final ErrorCode errorCode;

    public AuthenticationErrorCodeException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

}