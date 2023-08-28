package real.world.error.exception;

import real.world.error.ErrorCode;

public class UsernameInvalidException extends BusinessException{

    public UsernameInvalidException() {
        super(ErrorCode.USERNAME_INVALID);
    }

}
