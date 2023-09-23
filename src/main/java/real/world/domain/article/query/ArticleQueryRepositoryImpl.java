package real.world.domain.article.query;

import static real.world.domain.article.entity.QArticle.article;
import static real.world.domain.article.entity.QFavorite.favorite;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleQueryRepositoryImpl implements ArticleQueryRepository {

    private final JPAQueryFactory factory;

    public ArticleQueryRepositoryImpl(JPAQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<ArticleView> findById(Long loginId, Long id) {
        return Optional.ofNullable(factory.select(new QArticleView(
            article,
            JPAExpressions.selectFrom(favorite)
                .where(favorite.userId.eq(loginId).and(favorite.articleId.eq(id)))
                .exists(),
            JPAExpressions.select(favorite.count())
                .from(favorite)
                .where(favorite.articleId.eq(id))
        )).from(article).where(article.id.eq(id)).fetchOne());
    }

    @Override
    public Optional<ArticleView> findBySlug(Long loginId, String slug) {
        final Long id = factory.select(article.id)
            .from(article)
            .where(article.slug.eq(slug))
            .fetchOne();
        return findById(loginId, id);
    }

}
