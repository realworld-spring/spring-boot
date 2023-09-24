package real.world.domain.article.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import real.world.domain.article.entity.Article;

public interface ArticleRepository extends Repository<Article, Long> {

    Article save(Article article);

    Optional<Article> findById(Long id);

    Optional<Article> findBySlug(String slug);

    void delete(Article article);

}
