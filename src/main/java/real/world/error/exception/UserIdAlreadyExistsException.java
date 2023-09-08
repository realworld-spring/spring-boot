package real.world.error.exception;

import real.world.error.ErrorCode;

public class UserIdAlreadyExistsException extends BusinessException{

    public UserIdAlreadyExistsException() {
        super(ErrorCode.USERID_ALREADY_EXIST);
    }

}
