package real.world.fixture;

import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.JOHN;

import lombok.Getter;
import real.world.domain.follow.entity.Follow;

@Getter
public enum FollowFixtures {

    JOHN이_ALICE를_팔로우(JOHN, ALICE);

    private final UserFixtures follower;

    private final UserFixtures user;

    FollowFixtures(UserFixtures follower, UserFixtures user) {
        this.follower = follower;
        this.user = user;
    }

    public Follow 생성() {
        return new Follow(
            user.생성(),
            follower.생성()
        );
    }

}
