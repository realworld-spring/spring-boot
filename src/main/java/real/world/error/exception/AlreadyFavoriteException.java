package real.world.error.exception;

import real.world.error.ErrorCode;

public class AlreadyFavoriteException extends BusinessException {

    public AlreadyFavoriteException() {
        super(ErrorCode.ALREADY_FAVORITE);
    }

}
