package real.world.domain.article.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import real.world.domain.article.dto.response.ArticleResponse;
import real.world.domain.article.query.ArticleQueryRepository;
import real.world.domain.article.query.ArticleView;
import real.world.domain.global.Page;
import real.world.domain.profile.query.Profile;
import real.world.domain.profile.query.ProfileQueryRepository;
import real.world.error.exception.ArticleNotFoundException;
import real.world.error.exception.UserIdNotExistException;

@Service
@Transactional(readOnly = true)
public class ArticleQueryService {

    private final ArticleQueryRepository articleQueryRepository;

    private final ProfileQueryRepository profileQueryRepository;

    public ArticleQueryService(ArticleQueryRepository articleQueryRepository,
        ProfileQueryRepository profileQueryRepository) {
        this.articleQueryRepository = articleQueryRepository;
        this.profileQueryRepository = profileQueryRepository;
    }

    public ArticleResponse getArticle(Long loginId, Long articleId) {
        final ArticleView articleView = articleQueryRepository.findById(loginId, articleId)
            .orElseThrow(ArticleNotFoundException::new);
        setProfile(loginId, articleView);
        return ArticleResponse.of(articleView);
    }

    public ArticleResponse getArticle(Long loginId, String slug) {
        final ArticleView articleView = articleQueryRepository.findBySlug(loginId, slug)
            .orElseThrow(ArticleNotFoundException::new);
        setProfile(loginId, articleView);
        return ArticleResponse.of(articleView);
    }

    public List<ArticleResponse> getArticles(Long loginId, Page page) {
        final List<ArticleView> articleViews = articleQueryRepository.findByLoginId(loginId, page);
        setProfile(loginId, articleViews);
        return articleViews.stream().map(ArticleResponse::of).toList();
    }

    public List<ArticleResponse> getRecent(Long loginId, Page page, String tag, String author,
        String favorited) {
        final List<ArticleView> articleViews = articleQueryRepository.findRecent(loginId, page, tag,
            author, favorited);
        setProfile(loginId, articleViews);
        return articleViews.stream().map(ArticleResponse::of).toList();
    }

    private void setProfile(Long loginId, ArticleView articleView) {
        final Profile profile = profileQueryRepository.findByLoginIdAndUserId(loginId,
                articleView.getUserId()).orElseThrow(UserIdNotExistException::new);
        articleView.setProfile(profile);
    }

    private void setProfile(Long loginId, List<ArticleView> articleView) {
        final Set<Long> ids = articleView.stream().map(ArticleView::getUserId)
            .collect(Collectors.toSet());
        final Map<Long, Profile> profiles = profileQueryRepository.findByLoginIdAndIds(loginId,
            ids);
        articleView.forEach((av) -> av.setProfile(profiles.get(av.getUserId())));
    }

}
