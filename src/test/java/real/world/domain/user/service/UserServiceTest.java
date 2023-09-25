package real.world.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.request.UpdateRequest;
import real.world.domain.user.dto.response.UserDto;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.UserIdNotExistException;
import real.world.error.exception.UsernameAlreadyExistsException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final UserRepository userRepository = BDDMockito.mock(UserRepository.class);

    private final PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Nested
    class 회원가입은 {

        @Test
        void 정상_호출시_회원가입을_하고_응답을_반환한다() {
            // given
            final RegisterRequest request = JOHN.회원가입을_한다();

            // when
            final UserDto response = userService.register(request);

            // then
            assertAll(() -> {
                verify(userRepository).save(any());
                assertThat(response.getUsername()).isEqualTo(JOHN.getUsername());
                assertThat(response.getEmail()).isEqualTo(JOHN.getEmail());
            });
        }

        @Test
        void 유저네임_중복_시_예외를_던진다() {
            // given
            given(userRepository.existsByUsername(JOHN.getUsername())).willReturn(true);

            final RegisterRequest request = JOHN.회원가입을_한다();

            // when & then
            assertThatThrownBy(
                () -> userService.register(request)
            ).isInstanceOf(UsernameAlreadyExistsException.class);
        }

    }

    @Nested
    class 유저조회는 {

        @Test
        void 정상_호출시_유저정보를_찾아_응답을_반환한다() {
            // given
            final User user = JOHN.생성();
            final Long id = JOHN.getId();
            given(userRepository.findById(id)).willReturn(Optional.of(user));

            // when
            final UserDto response = userService.getUser(id);

            // then
            assertAll(() -> {
                verify(userRepository).findById(any());
                assertThat(response.getUsername()).isEqualTo(user.getUsername());
                assertThat(response.getEmail()).isEqualTo(user.getEmail());
                assertThat(response.getBio()).isEqualTo(user.getBio());
                assertThat(response.getImage()).isEqualTo(user.getImageUrl());
            });
        }

        @Test
        void 유저ID가_존재하지_않는다면_예외를_던진다() {
            // given
            final Long id = 1L;
            given(userRepository.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(
                () -> userService.getUser(id)
            ).isInstanceOf(UserIdNotExistException.class);
        }

    }

    @Nested
    class 유저수정은 {

        @Test
        void 정상_호출시_유저_수정을_하고_응답을_반환한다() {
            // given
            final Long id = JOHN.getId();
            final User user = JOHN.생성();
            final UpdateRequest request = JOHN.유저수정_요청();
            given(userRepository.findById(id)).willReturn(Optional.of(user));

            // when
            final UserDto response = userService.update(id, request);

            // then
            assertAll(() -> {
                verify(userRepository).findById(anyLong());
                assertThat(response.getUsername()).isEqualTo(request.getUsername());
                assertThat(response.getEmail()).isEqualTo(request.getEmail());
                assertThat(response.getBio()).isEqualTo(request.getBio());
                assertThat(response.getImage()).isEqualTo(request.getImage());
            });
        }

        @Test
        void 유저ID가_존재하지_않는다면_예외를_던진다() {
            // given
            final Long id = JOHN.getId();
            final UpdateRequest request = JOHN.유저수정_요청();
            given(userRepository.findById(id)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(
                () -> userService.update(id, request)
            ).isInstanceOf(UserIdNotExistException.class);
        }

    }

}