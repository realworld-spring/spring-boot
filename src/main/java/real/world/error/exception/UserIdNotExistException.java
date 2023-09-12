package real.world.error.exception;

import real.world.error.ErrorCode;

public class UserIdNotExistException extends BusinessException{

    public UserIdNotExistException() {
        super(ErrorCode.USERID_NOT_EXIST);
    }

}
