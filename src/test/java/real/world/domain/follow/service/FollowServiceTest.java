package real.world.domain.follow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import real.world.domain.profile.dto.response.ProfileResponse;
import real.world.domain.follow.entity.Follow;
import real.world.domain.follow.repository.FollowRepository;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.UsernameNotExistException;

class FollowServiceTest {

    private final UserRepository userRepository = BDDMockito.mock(UserRepository.class);

    private final FollowRepository followRepository = BDDMockito.mock(FollowRepository.class);

    private final FollowService followService = new FollowService(userRepository, followRepository);

    @Nested
    class 팔로우_요청은 {

        @Test
        void 정상_호출시_팔로우를_하고_응답을_반환한다() {
            // given
            final User user = JOHN.생성();
            final User follower = ALICE.생성();
            given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
            given(userRepository.findById(follower.getId())).willReturn(Optional.of(follower));
            given(followRepository.existsByUserIdAndFollowerId(user.getId(), follower.getId())).willReturn(false);

            // when
            final ProfileResponse response = followService.follow(follower.getId(), user.getUsername());

            // then
            assertAll(() -> {
                verify(followRepository).save(any(Follow.class));
                assertThat(response.getUsername()).isEqualTo(user.getUsername());
                assertThat(response.getBio()).isEqualTo(user.getBio());
                assertThat(response.getImage()).isEqualTo(user.getImageUrl());
                assertThat(response.isFollowing()).isEqualTo(true);
            });
        }

        @Test
        void 유저가_존재하지_않을_시_에외를_던진다() {
            // given
            final User user = JOHN.생성();
            final User follower = ALICE.생성();
            given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(
                () -> followService.follow(follower.getId(), user.getUsername())
            ).isInstanceOf(UsernameNotExistException.class);
        }

    }

    @Nested
    class 팔로우_취소_요청은 {

        @Test
        void 정상_호출시_팔로우를_취소하고_응답을_반환한다() {
            // given
            final User user = JOHN.생성();
            final User follower = ALICE.생성();
            given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
            given(userRepository.findById(follower.getId())).willReturn(Optional.of(follower));
            given(followRepository.existsByUserIdAndFollowerId(user.getId(), follower.getId())).willReturn(true);

            // when
            final ProfileResponse response = followService.unfollow(follower.getId(), user.getUsername());

            // then
            assertAll(() -> {
                verify(followRepository).delete(any(Follow.class));
                assertThat(response.getUsername()).isEqualTo(user.getUsername());
                assertThat(response.getBio()).isEqualTo(user.getBio());
                assertThat(response.getImage()).isEqualTo(user.getImageUrl());
                assertThat(response.isFollowing()).isEqualTo(false);
            });
        }

    }

}