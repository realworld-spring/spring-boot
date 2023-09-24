package real.world.error.exception;

import real.world.error.ErrorCode;

public class UsernameNotExistException extends BusinessException{

    public UsernameNotExistException() {
        super(ErrorCode.USERNAME_NOT_EXIST);
    }

}