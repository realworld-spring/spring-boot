package real.world.error.exception;

import real.world.error.ErrorCode;

public class AlreadyFollowingException extends BusinessException{

    public AlreadyFollowingException() {
        super(ErrorCode.ALREADY_FOLLOWING);
    }

}