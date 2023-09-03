package real.world.error.exception;

import real.world.error.ErrorCode;

public class AuthenticationFailException extends BusinessException{

    public AuthenticationFailException() { super(ErrorCode.AUTHENTICATION_FAIL); }

}
