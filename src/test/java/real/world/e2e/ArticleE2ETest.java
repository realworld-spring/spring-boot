package real.world.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static real.world.e2e.RestAssuredUtils.로그인_상태로_POST_요청을_보낸다;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.UserFixtures.JOHN;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.dto.response.ArticleResponse;
import real.world.e2e.util.DBInitializer;

public class ArticleE2ETest extends E2ETest {

    @Autowired
    private DBInitializer dbInitializer;

    @Test
    void 업로드를_한다() {
        // given
        dbInitializer.유저들이_회원가입_돼있다();
        final UploadRequest request = 게시물.업로드를_한다();

        // when
        final ExtractableResponse<Response> extractableResponse = 로그인_상태로_POST_요청을_보낸다(
            JOHN, "/articles", request);
        final ArticleResponse response = extractableResponse.as(ArticleResponse.class);

        // then
        assertAll(() -> {
            assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.getTitle()).isEqualTo(게시물.getTitle());
            assertThat(response.getDescription()).isEqualTo(게시물.getDescription());
            assertThat(response.getBody()).isEqualTo(게시물.getBody());
        });
    }

}
