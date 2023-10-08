package real.world.error.exception;

import real.world.error.ErrorCode;

public class FavoriteNotFoundException extends BusinessException {

    public FavoriteNotFoundException() {
        super(ErrorCode.FAVORITE_NOT_FOUND);
    }

}
