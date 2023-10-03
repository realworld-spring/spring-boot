package real.world.domain.profile.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import real.world.domain.follow.entity.Follow;
import real.world.domain.follow.repository.FollowRepository;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.support.QueryRepositoryTest;

@Import(ProfileQueryRepositoryImpl.class)
class ProfileQueryRepositoryTest extends QueryRepositoryTest {

    @Autowired
    private ProfileQueryRepository profileQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Test
    void 팔로우중인_유저의_프로필_가져오기() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());
        followRepository.save(new Follow(alice, john));
        final Long loginId = john.getId();
        final String username = ALICE.getUsername();

        // when
        final Optional<Profile> result = profileQueryRepository.findByLoginIdAndUsername(loginId, username);

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            final Profile profile = result.get();
            assertThat(profile.getUsername()).isEqualTo(ALICE.getUsername());
            assertThat(profile.getBio()).isEqualTo(ALICE.getBio());
            assertThat(profile.getImage()).isEqualTo(ALICE.getImageUrl());
            assertThat(profile.isFollowing()).isEqualTo(true);
        });
    }

    @Test
    void 팔로우중이_아닌_유저의_프로필_가져오기() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());
        Long loginId = JOHN.getId();
        String username = ALICE.getUsername();

        // when
        Optional<Profile> result = profileQueryRepository.findByLoginIdAndUsername(loginId, username);

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            final Profile profile = result.get();
            assertThat(profile.getUsername()).isEqualTo(ALICE.getUsername());
            assertThat(profile.getBio()).isEqualTo(ALICE.getBio());
            assertThat(profile.getImage()).isEqualTo(ALICE.getImageUrl());
            assertThat(profile.isFollowing()).isEqualTo(false);
        });

    }

}