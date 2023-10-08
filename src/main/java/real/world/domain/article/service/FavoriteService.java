package real.world.domain.article.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import real.world.domain.article.entity.Article;
import real.world.domain.article.entity.Favorite;
import real.world.domain.article.repository.ArticleRepository;
import real.world.domain.article.repository.FavoriteRepository;
import real.world.error.exception.AlreadyFavoriteException;
import real.world.error.exception.ArticleNotFoundException;
import real.world.error.exception.FavoriteNotFoundException;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final ArticleRepository articleRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, ArticleRepository articleRepository) {
        this.favoriteRepository = favoriteRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Long favorite(Long loginId, String slug) {
        final Article article = articleRepository.findBySlug(slug)
            .orElseThrow(ArticleNotFoundException::new);
        if (favoriteRepository.existsByUserIdAndArticleId(loginId, article.getId())) {
            throw new AlreadyFavoriteException();
        }
        favoriteRepository.save(new Favorite(loginId, article.getId()));
        return article.getId();
    }

    @Transactional
    public Long unfavorite(Long loginId, String slug) {
        final Article article = articleRepository.findBySlug(slug)
            .orElseThrow(ArticleNotFoundException::new);
        final Favorite favorite = favoriteRepository.findByUserIdAndArticleId(loginId, article.getId())
            .orElseThrow(FavoriteNotFoundException::new);
        favoriteRepository.delete(favorite);
        return article.getId();
    }

}
