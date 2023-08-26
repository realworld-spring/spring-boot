package real.world.error.exception;

import real.world.error.ErrorCode;

public class UsernameAlreadyExistsException extends BusinessException{

    public UsernameAlreadyExistsException() {
        super(ErrorCode.USERNAME_ALREADY_EXIST);
    }

}
