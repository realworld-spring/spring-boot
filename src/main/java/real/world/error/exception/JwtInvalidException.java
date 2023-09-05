package real.world.error.exception;

import real.world.error.ErrorCode;

public class JwtInvalidException extends BusinessException{

    public JwtInvalidException() {
        super(ErrorCode.JWT_INVALID);
    }

}
