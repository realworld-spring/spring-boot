package real.world.error.exception;

import real.world.error.ErrorCode;

public class ArticleNotFoundException extends BusinessException {

    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }

}
