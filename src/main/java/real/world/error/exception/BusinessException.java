package real.world.error.exception;

import org.springframework.http.HttpStatus;
import real.world.error.ErrorCode;

public abstract class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getCode();
    }

    public String getBody() {
        return errorCode.getBody();
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

}
