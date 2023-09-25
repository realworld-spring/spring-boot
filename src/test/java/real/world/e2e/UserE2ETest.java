package real.world.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static real.world.e2e.RestAssuredUtils.POST_요청을_보낸다;
import static real.world.fixture.UserFixtures.JOHN;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.response.UserApiResponse;

public class UserE2ETest extends E2ETest {

    @Test
    void 회원가입을_한다() {
        // given
        final RegisterRequest request = JOHN.회원가입을_한다();

        // when
        final ExtractableResponse<Response> extractableResponse = POST_요청을_보낸다("/users",
            request);
        final UserApiResponse response = extractableResponse.as(UserApiResponse.class);

        // then
        assertAll(() -> {
            assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.getUser().getUsername()).isEqualTo(JOHN.getUsername());
            assertThat(response.getUser().getEmail()).isEqualTo(JOHN.getEmail());
        });
    }

}
