package real.world.error.exception;

import real.world.error.ErrorCode;

public class ArticleUnauthorizedException extends BusinessException {

    public ArticleUnauthorizedException() {
        super(ErrorCode.ARTICLE_UNAUTHORIZED);
    }

}
