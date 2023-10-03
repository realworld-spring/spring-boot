package real.world.domain.profile.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.BOB;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import real.world.domain.follow.entity.Follow;
import real.world.domain.follow.repository.FollowRepository;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.fixture.UserFixtures;
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
    void 유저네임을_이용해_팔로우중인_유저의_프로필_가져온다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());
        followRepository.save(new Follow(alice, john));

        Long loginId = john.getId();

        // when
        Optional<Profile> result = profileQueryRepository.findByLoginIdAndUsername(loginId, ALICE.getUsername());

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            assertCorrectProfile(result.get(), ALICE, true);
        });
    }

    @Test
    void 유저네임을_이용해_팔로우중이_아닌_유저의_프로필_가져온다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());

        Long loginId = john.getId();

        // when
        Optional<Profile> result = profileQueryRepository.findByLoginIdAndUsername(loginId, ALICE.getUsername());

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            assertCorrectProfile(result.get(), ALICE, false);
        });

    }

    @Test
    void 유저id를_이용해_팔로우중인_유저의_프로필_가져온다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());
        followRepository.save(new Follow(alice, john));

        Long loginId = john.getId();

        // when
        Optional<Profile> result = profileQueryRepository.findByLoginIdAndUserId(loginId, alice.getId());

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            assertCorrectProfile(result.get(), ALICE, true);
        });
    }

    @Test
    void 유저id를_이용해_팔로우중이_아닌_유저의_프로필_가져온다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());

        Long loginId = john.getId();

        // when
        Optional<Profile> result = profileQueryRepository.findByLoginIdAndUserId(loginId, alice.getId());

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            assertCorrectProfile(result.get(), ALICE, false);
        });

    }

    @Test
    void 타겟_유저들의_프로필_가져온다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());
        final User bob = userRepository.save(BOB.ID없이_생성());
        followRepository.save(new Follow(alice, john));
        followRepository.save(new Follow(bob, john));

        Long loginId = john.getId();

        List<UserFixtures> userFixtures = List.of(ALICE, BOB);
        Set<Long> ids = Set.of(alice.getId(), bob.getId());

        // when
        Map<Long, Profile> result = profileQueryRepository.findByLoginIdAndIds(loginId,
            ids);

        // then
        assertAll(() -> {
            assertThat(result).hasSize(2);
            assertCorrectProfile(result.get(alice.getId()), ALICE, true);
            assertCorrectProfile(result.get(bob.getId()), BOB, true);
        });

    }

    private void assertCorrectProfile(Profile resultProfile, UserFixtures userFixture, boolean isFollowing) {
        assertAll(() -> {
            assertThat(resultProfile.getUsername()).isEqualTo(userFixture.getUsername());
            assertThat(resultProfile.getBio()).isEqualTo(userFixture.getBio());
            assertThat(resultProfile.getImage()).isEqualTo(userFixture.getImageUrl());
            assertThat(resultProfile.isFollowing()).isEqualTo(isFollowing);
        });
    }

}