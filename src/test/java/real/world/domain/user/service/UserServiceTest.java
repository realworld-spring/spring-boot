package real.world.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static real.world.fixture.UserFixtures.JOHN;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.response.RegisterResponse;
import real.world.domain.user.repository.UserRepository;
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
            final RegisterResponse response = userService.register(request);

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

}
