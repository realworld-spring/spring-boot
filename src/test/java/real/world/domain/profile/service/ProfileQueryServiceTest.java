package real.world.domain.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import real.world.domain.profile.dto.response.ProfileResponse;
import real.world.domain.profile.repository.ProfileQueryRepository;
import real.world.domain.user.dto.ProfileDto;
import real.world.domain.user.entity.User;

class ProfileQueryServiceTest {

    private final ProfileQueryRepository profileQueryRepository = BDDMockito.mock(ProfileQueryRepository.class);

    private final ProfileQueryService profileQueryService = new ProfileQueryService(profileQueryRepository);

    @Nested
    class 프로필_요청은 {

        @Test
        void 로그인이_되어있고_정상_호출시_프로필을_반환한다() {
            // given
            final User user = JOHN.생성();
            final Long followerId = ALICE.생성().getId();
            final ProfileDto profile = ProfileDto.of(user, true);
            given(profileQueryRepository.findByLoginIdAndUsername(eq(followerId), eq(user.getUsername()))).willReturn(
                Optional.of(profile));

            // when
            final ProfileResponse response = profileQueryService.getProfile(followerId, user.getUsername());

            // then
            assertAll(() -> {
                verify(profileQueryRepository).findByLoginIdAndUsername(anyLong(), anyString());
                assertThat(response.getUsername()).isEqualTo(user.getUsername());
                assertThat(response.getBio()).isEqualTo(user.getBio());
                assertThat(response.getImage()).isEqualTo(user.getImageUrl());
                assertThat(response.isFollowing()).isEqualTo(true);
            });
        }

        @Test
        void 로그인이_되어있지_않고_정상_호출시_프로필을_반환한다() {
            // given
            final User user = JOHN.생성();
            final Long notExistId = 0L;
            final ProfileDto profile = ProfileDto.of(user, false);
            given(profileQueryRepository.findByLoginIdAndUsername(eq(notExistId), eq(user.getUsername()))).willReturn(
                Optional.of(profile));

            // when
            final ProfileResponse response = profileQueryService.getProfile(notExistId, user.getUsername());

            // then
            assertAll(() -> {
                verify(profileQueryRepository).findByLoginIdAndUsername(anyLong(), anyString());
                assertThat(response.getUsername()).isEqualTo(user.getUsername());
                assertThat(response.getBio()).isEqualTo(user.getBio());
                assertThat(response.getImage()).isEqualTo(user.getImageUrl());
                assertThat(response.isFollowing()).isEqualTo(false);
            });
        }

    }
}