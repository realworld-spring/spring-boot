package real.world.domain.article.query;

import static real.world.domain.article.entity.QArticle.article;
import static real.world.domain.article.entity.QFavorite.favorite;
import static real.world.domain.follow.entity.QFollow.follow;
import static real.world.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import real.world.domain.global.Page;

@Repository
@Slf4j
public class ArticleQueryRepositoryImpl implements ArticleQueryRepository {

    private final JPAQueryFactory factory;

    public ArticleQueryRepositoryImpl(JPAQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<ArticleView> findById(Long loginId, Long id) {
        return Optional.ofNullable(factory.select(Projections.constructor(ArticleView.class,
                article,
                JPAExpressions.selectFrom(favorite)
                    .where(favorite.userId.eq(loginId).and(favorite.articleId.eq(id)))
                    .exists(),
                JPAExpressions.select(favorite.count())
                    .from(favorite)
                    .where(favorite.articleId.eq(id))
            ))
            .from(article)
            .where(article.id.eq(id))
            .fetchOne());
    }

    @Override
    public List<ArticleView> findByLoginId(Long loginId, Page page) {
        return factory.select(Projections.constructor(ArticleView.class,
                article,
                JPAExpressions.selectFrom(favorite)
                    .where(favorite.userId.eq(loginId).and(favorite.articleId.eq(article.id)))
                    .exists(),
                JPAExpressions.select(favorite.count())
                    .from(favorite)
                    .where(favorite.articleId.eq(article.id))
            ))
            .from(article)
            .where(article.userId.in(findFollowingUserIds(loginId)))
            .offset(page.getOffset())
            .limit(page.getLimit())
            .fetch();
    }

    private JPQLQuery<Long> findFollowingUserIds(Long loginId) {
        return JPAExpressions.select(follow.user.id)
            .from(follow)
            .where(follow.follower.id.eq(loginId));
    }

    @Override
    public List<ArticleView> findRecent(Long loginId, Page page, String tag, String author,
        String favorited) {
        return factory.select(Projections.constructor(ArticleView.class,
                article,
                JPAExpressions.selectFrom(favorite)
                    .where(favorite.userId.eq(loginId).and(favorite.articleId.eq(article.id)))
                    .exists(),
                JPAExpressions.select(favorite.count())
                    .from(favorite)
                    .where(favorite.articleId.eq(article.id))
            ))
            .from(article)
            .where(eqAuthor(author), hasTag(tag), isFavorited(favorited))
            .offset(page.getOffset())
            .limit(page.getLimit())
            .fetch();
    }

    private BooleanExpression eqAuthor(String author) {
        if (author == null) {
            return null;
        }
        final Long authorId = factory.select(user.id)
            .from(user)
            .where(user.username.eq(author))
            .fetchFirst();
        return authorId == null ? null : article.userId.eq(authorId);
    }

    private BooleanExpression hasTag(String tag) {
        return tag == null ? null : article.tags.contains(tag);
    }

    private BooleanExpression isFavorited(String favorited) {
        if (favorited == null) {
            return null;
        }
        final List<Long> favoriteArticleIds = factory.select(favorite.articleId)
            .from(favorite)
            .innerJoin(user).on(user.id.eq(favorite.userId))
            .where(user.username.eq(favorited))
            .fetch();
        if(favoriteArticleIds.isEmpty()) {
            return null;
        }
        return article.id.in(favoriteArticleIds);
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
