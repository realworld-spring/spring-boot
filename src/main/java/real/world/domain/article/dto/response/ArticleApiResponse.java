package real.world.domain.article.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ArticleApiResponse {

    private ArticleDto article;

    public ArticleApiResponse(ArticleDto article) {
        this.article = article;
    }

}
