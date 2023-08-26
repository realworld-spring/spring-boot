package real.world.domain.user.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import real.world.error.exception.EmailInvalidException;
import real.world.error.exception.UsernameInvalidException;

public class UserTest {

    @Test
    void 회원의_username이_너무_길면_예외를_던진다() {
        // given & when & then
        assertThatThrownBy(
            () -> new User(
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "1234",
                "aaa@email.com",
                "",
                "image.png"
            )
        ).isInstanceOf(UsernameInvalidException.class);
    }

    @Test
    void 회원의_email이_너무_길면_예외를_던진다() {
        // given & when & then
        assertThatThrownBy(
            () -> new User(
                "john",
                "1234",
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@email.com",
                "",
                "image.png"
            )
        ).isInstanceOf(EmailInvalidException.class);
    }

    @Test
    void 회원의_email이_잘못된_형식이면_예외를_던진다() {
        // given & when & then
        assertThatThrownBy(
            () -> new User(
                "john",
                "1234",
                "aaaaemail.com",
                "",
                "image.png"
            )
        ).isInstanceOf(EmailInvalidException.class);
    }

}
