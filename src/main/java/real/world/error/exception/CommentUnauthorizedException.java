package real.world.error.exception;

import real.world.error.ErrorCode;

public class CommentUnauthorizedException extends BusinessException {

    public CommentUnauthorizedException() {
        super(ErrorCode.COMMENT_UNAUTHORIZED);
    }

}