package real.world.domain.comment.query;

import static real.world.domain.article.entity.QArticle.article;
import static real.world.domain.comment.entity.QComment.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory factory;

    public CommentQueryRepositoryImpl(JPAQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<CommentView> findBySlug(String slug) {
        return factory.select(Projections.constructor(CommentView.class,
                comment
            ))
            .from(comment)
            .where(eqArticleSlug(slug))
            .fetch();
    }

    private BooleanExpression eqArticleSlug(String slug) {
        final Long articleId = factory.select(article.id)
            .from(article)
            .where(article.slug.eq(slug))
            .fetchOne();
        return comment.articleId.eq(articleId);
    }

}