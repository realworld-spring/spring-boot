package real.world.domain.profile.query;

import static real.world.domain.follow.entity.QFollow.follow;
import static real.world.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileQueryRepositoryImpl implements ProfileQueryRepository {

    public final JPAQueryFactory factory;

    public ProfileQueryRepositoryImpl(JPAQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<Profile> findByLoginIdAndUsername(Long loginId, String username) {
        return Optional.ofNullable(
            factory
                .select(
                    new QProfile(
                        user,
                        JPAExpressions.
                            selectFrom(follow)
                            .where(follow.follower.id.eq(loginId)
                                .and(follow.user.username.eq(username)))
                            .exists()))
                .from(user).
                where(user.username.eq(username)).
                fetchOne());
    }

    @Override
    public Optional<Profile> findByLoginIdAndUserId(Long loginId, Long userId) {
        return Optional.ofNullable(
            factory
                .select(
                    new QProfile(
                        user,
                        JPAExpressions.
                            selectFrom(follow)
                            .where(follow.follower.id.eq(loginId)
                                .and(follow.user.id.eq(userId)))
                            .exists()))
                .from(user).
                where(user.id.eq(userId)).
                fetchOne());
    }

    @Override
    public Map<Long, Profile> findByLoginIdAndIds(Long loginId, Set<Long> ids) {
        return factory.select(Projections.constructor(Profile.class,
                user,
                JPAExpressions.
                    selectFrom(follow)
                    .where(follow.follower.id.eq(loginId).and(follow.user.id.eq(user.id)))
                    .exists())
            )
            .from(user)
            .where(user.id.in(ids))
            .fetch().stream().collect(Collectors.toMap(Profile::getId, Function.identity()));
    }

}