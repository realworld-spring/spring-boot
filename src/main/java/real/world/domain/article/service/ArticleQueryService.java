package real.world.domain.article.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import real.world.domain.article.dto.response.ArticleResponse;
import real.world.domain.article.query.ArticleQueryRepository;
import real.world.domain.article.query.ArticleView;
import real.world.error.exception.ArticleNotFoundException;

@Service
@Transactional(readOnly = true)
public class ArticleQueryService {

    private final ArticleQueryRepository articleQueryRepository;

    public ArticleQueryService(ArticleQueryRepository articleQueryRepository) {
        this.articleQueryRepository = articleQueryRepository;
    }

    public ArticleResponse getArticle(Long loginId, Long articleId) {
        final ArticleView articleView = articleQueryRepository.findById(loginId, articleId)
            .orElseThrow(ArticleNotFoundException::new);
        return ArticleResponse.of(articleView);
    }

    public ArticleResponse getArticle(Long loginId, String slug) {
        final ArticleView articleView = articleQueryRepository.findBySlug(loginId, slug)
            .orElseThrow(ArticleNotFoundException::new);
        return ArticleResponse.of(articleView);
    }

}
