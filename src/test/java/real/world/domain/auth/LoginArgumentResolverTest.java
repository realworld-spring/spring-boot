package real.world.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import real.world.domain.auth.annotation.LoginArgumentResolver;

@ExtendWith(SpringExtension.class)
public class LoginArgumentResolverTest {

    private final LoginArgumentResolver argumentResolver = new LoginArgumentResolver();

    @Test
    @WithMockUser("username")
    void 인증된_사용자라면_PRINCIPAL을_반환한다() throws Exception {
        // given & when
        final User principal = (User) argumentResolver.resolveArgument(null, null, null, null);

        // then
        assertThat(principal != null).isTrue();
        assertThat(principal.getUsername()).isEqualTo("username");
    }

}
