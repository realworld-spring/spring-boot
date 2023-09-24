package real.world.error.exception;

import real.world.error.ErrorCode;

public class FollowNotExistException extends BusinessException{

    public FollowNotExistException() {
        super(ErrorCode.FOLLOW_NOT_EXIST);
    }

}