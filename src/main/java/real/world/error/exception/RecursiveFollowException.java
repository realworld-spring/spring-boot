package real.world.error.exception;

import real.world.error.ErrorCode;

public class RecursiveFollowException extends BusinessException{

    public RecursiveFollowException() {
        super(ErrorCode.RECURSIVE_FOLLOW);
    }

}