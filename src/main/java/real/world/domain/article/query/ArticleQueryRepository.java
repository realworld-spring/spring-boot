package real.world.domain.article.query;

import java.util.List;
import java.util.Optional;
import real.world.domain.global.Page;

public interface ArticleQueryRepository {

    Optional<ArticleView> findById(Long loginId, Long id);

    List<ArticleView> findByLoginId(Long loginId, Page page);

    List<ArticleView> findRecent(Long loginId, Page page, String tag, String author, String favorited);

    Optional<ArticleView> findBySlug(Long loginId, String slug);

}
