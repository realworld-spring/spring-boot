package real.world.domain.article.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleApiResponse {

    private ArticleResponse article;

    public ArticleApiResponse(ArticleResponse article) {
        this.article = article;
    }

}
