package real.world.error.exception;

import real.world.error.ErrorCode;

public class AlreadyFollowedException extends BusinessException{

    public AlreadyFollowedException() {
        super(ErrorCode.ALREADY_FOLLOWED);
    }

}