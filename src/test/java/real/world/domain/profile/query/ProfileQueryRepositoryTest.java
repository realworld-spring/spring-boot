package real.world.domain.profile.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.BOB;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import real.world.e2e.util.DBInitializer;
import real.world.fixture.UserFixtures;
import real.world.support.QueryRepositoryTest;

@Import(ProfileQueryRepositoryImpl.class)
class ProfileQueryRepositoryTest extends QueryRepositoryTest {

    @Autowired
    private ProfileQueryRepository profileQueryRepository;

    @Autowired
    private DBInitializer dbInitializer;

    @Test
    void 유저네임을_이용해_팔로우중인_유저의_프로필_가져온다() {
        // given
        dbInitializer.JOHN이_ALICE를_팔로우한다();

        Long loginId = JOHN.getId();
        UserFixtures userFixture = ALICE;

        // when
        Optional<Profile> result = profileQueryRepository.findByLoginIdAndUsername(loginId, userFixture.getUsername());

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            assertCorrectProfile(result.get(), userFixture, true);
        });
    }

    @Test
    void 유저네임을_이용해_팔로우중이_아닌_유저의_프로필_가져온다() {
        // given
        dbInitializer.유저들이_회원가입_돼있다();

        Long loginId = JOHN.getId();
        UserFixtures userFixture = ALICE;

        // when
        Optional<Profile> result = profileQueryRepository.findByLoginIdAndUsername(loginId, userFixture.getUsername());

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            assertCorrectProfile(result.get(), userFixture, false);
        });

    }

    @Test
    void 유저id를_이용해_팔로우중인_유저의_프로필_가져온다() {
        // given
        dbInitializer.JOHN이_ALICE를_팔로우한다();

        Long loginId = JOHN.getId();
        UserFixtures userFixture = ALICE;

        // when
        Optional<Profile> result = profileQueryRepository.findByLoginIdAndUserId(loginId, userFixture.getId());

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            assertCorrectProfile(result.get(), userFixture, true);
        });
    }

    @Test
    void 유저id를_이용해_팔로우중이_아닌_유저의_프로필_가져온다() {
        // given
        dbInitializer.유저들이_회원가입_돼있다();

        Long loginId = JOHN.getId();
        UserFixtures userFixture = ALICE;

        // when
        Optional<Profile> result = profileQueryRepository.findByLoginIdAndUserId(loginId, userFixture.getId());

        // then
        assertAll(() -> {
            assertThat(result.isPresent()).isTrue();
            assertCorrectProfile(result.get(), userFixture, false);
        });

    }

    @Test
    void 타겟_유저들의_프로필_가져온다() {
        // given
        dbInitializer.JOHN이_ALICE와_BOB을_팔로우한다();

        Long loginId = JOHN.getId();
        List<UserFixtures> userFixtures = List.of(ALICE, BOB);
        Set<Long> ids = userFixtures.stream().map(UserFixtures::getId)
            .collect(Collectors.toSet());

        // when
        Map<Long, Profile> result = profileQueryRepository.findByLoginIdAndIds(loginId,
            ids);

        // then
        userFixtures.forEach((userFixture) ->
            assertCorrectProfile(result.get(userFixture.getId()), userFixture, true)
        );

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