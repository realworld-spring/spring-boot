package real.world.domain.article.query;

import java.util.Optional;

public interface ArticleQueryRepository{

    Optional<ArticleView> findById(Long loginId, Long id);

}
