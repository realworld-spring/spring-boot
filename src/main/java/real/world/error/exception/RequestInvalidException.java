package real.world.error.exception;

import real.world.error.ErrorCode;

public class RequestInvalidException extends BusinessException {

    public RequestInvalidException() {
        super(ErrorCode.REQUEST_INVALID);
    }

}
