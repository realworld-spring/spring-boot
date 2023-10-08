package real.world.domain.article.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import real.world.domain.article.entity.Favorite;
import real.world.domain.article.entity.Favorite.FavoriteId;

public interface FavoriteRepository extends Repository<Favorite, FavoriteId> {

    Favorite save(Favorite favorite);

    boolean existsByUserIdAndArticleId(Long userId, Long articleId);

    Optional<Favorite> findByUserIdAndArticleId(Long userId, Long articleId);

    void delete(Favorite favorite);

}
