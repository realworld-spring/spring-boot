package real.world.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static real.world.e2e.RestAssuredUtils.POST_요청을_보낸다;
import static real.world.fixture.UserFixtures.JOHN;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.response.RegisterResponse;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class UserE2ETest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        RestAssured.config = RestAssured.config()
            .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (cls, charset) -> objectMapper
            ));
    }

    @Test
    void 회원가입을_한다() {
        // given
        final RegisterRequest request = JOHN.회원가입을_한다();

        // when
        final ExtractableResponse<Response> extractableResponse = POST_요청을_보낸다("/api/users",
            request);
        final RegisterResponse response = extractableResponse.as(RegisterResponse.class);

        // then
        assertAll(() -> {
            assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.getUsername()).isEqualTo(JOHN.getUsername());
            assertThat(response.getEmail()).isEqualTo(JOHN.getEmail());
        });
    }

}
