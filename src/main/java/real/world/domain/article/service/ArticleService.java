package real.world.domain.article.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import real.world.domain.article.dto.request.ArticleUpdateRequest;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.entity.Article;
import real.world.domain.article.repository.ArticleRepository;
import real.world.error.exception.ArticleNotFoundException;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final SlugTranslator slugTranslator;

    public ArticleService(ArticleRepository articleRepository, SlugTranslator slugTranslator) {
        this.articleRepository = articleRepository;
        this.slugTranslator = slugTranslator;
    }

    @Transactional
    public Long upload(Long loginId, UploadRequest request) {
        final Article article = requestToEntity(loginId, request);
        articleRepository.save(article);
        return article.getId();
    }

    @Transactional
    public Long update(Long loginId, String slug, ArticleUpdateRequest request) {
        final Article updateArticle = requestToEntity(loginId, request);
        final Article article = articleRepository.findBySlug(slug)
            .orElseThrow(ArticleNotFoundException::new);
        article.verifyUserId(loginId);
        article.update(updateArticle);
        return article.getId();
    }

    @Transactional
    public void delete(Long loginId, String slug) {
        final Article article = articleRepository.findBySlug(slug)
            .orElseThrow(ArticleNotFoundException::new);
        article.verifyUserId(loginId);
        articleRepository.delete(article);
    }

    private Article requestToEntity(Long loginId, UploadRequest request) {
        return new Article(
            loginId,
            request.getTitle(),
            slugTranslator,
            request.getDescription(),
            request.getBody(),
            request.getTags()
        );
    }

    private Article requestToEntity(Long loginId, ArticleUpdateRequest request) {
        return new Article(
            loginId,
            request.getTitle(),
            slugTranslator,
            request.getDescription(),
            request.getBody()
        );
    }

}
