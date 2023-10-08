package real.world.domain.article.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MultipleArticleApiResponse {

    private List<ArticleResponse> articles;

    private int articlesCount;

    public MultipleArticleApiResponse(List<ArticleResponse> articles) {
        this.articles = articles;
        this.articlesCount = articles.size();
    }

}
