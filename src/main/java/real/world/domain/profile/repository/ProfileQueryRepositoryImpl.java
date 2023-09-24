package real.world.domain.profile.repository;

import static real.world.domain.follow.entity.QFollow.follow;
import static real.world.domain.user.entity.QUser.user;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import real.world.domain.user.dto.ProfileDto;
import real.world.domain.user.dto.QProfileDto;

@Repository
public class ProfileQueryRepositoryImpl implements ProfileQueryRepository {

    public final JPAQueryFactory factory;

    public ProfileQueryRepositoryImpl(JPAQueryFactory factory) {
        this.factory = factory;
    }

    @Override
    public Optional<ProfileDto> findByLoginIdAndUsername(Long loginId, String username) {
        return Optional.ofNullable(
            factory
                .select(
                    new QProfileDto(
                        user,
                        JPAExpressions.
                            selectFrom(follow)
                            .where(follow.follower.id.eq(loginId).and(follow.user.username.eq(username)))
                            .exists()))
                .from(user).
                where(user.username.eq(username)).
                fetchOne());
    }

}