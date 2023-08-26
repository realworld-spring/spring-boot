package real.world.error.exception;

import real.world.error.ErrorCode;

public class EmailInvalidException extends BusinessException{

    public EmailInvalidException() {
        super(ErrorCode.EMAIL_INVALID);
    }

}
