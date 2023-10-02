package real.world.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static real.world.e2e.RestAssuredUtils.로그인_상태로_GET_요청을_보낸다;
import static real.world.e2e.RestAssuredUtils.로그인_상태로_POST_요청을_보낸다;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.JOHN;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import real.world.domain.profile.dto.response.ProfileApiResponse;
import real.world.e2e.util.DBInitializer;

public class ProfileE2ETest extends E2ETest {

    @Autowired
    private DBInitializer dbInitializer;

    @Test
    void 팔로우중인_유저의_프로필을_가져온다() {
        // given
        dbInitializer.JOHN이_ALICE를_팔로우한다();

        // when
        final ExtractableResponse<Response> extractableResponse = 로그인_상태로_GET_요청을_보낸다(
            JOHN, "/profiles/" + ALICE.getUsername());
        final ProfileApiResponse response = extractableResponse.as(ProfileApiResponse.class);

        // then
        assertAll(() -> {
            assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getProfile().getUsername()).isEqualTo(ALICE.getUsername());
            assertThat(response.getProfile().getBio()).isEqualTo(ALICE.getBio());
            assertThat(response.getProfile().getImage()).isEqualTo(ALICE.getImageUrl());
            assertThat(response.getProfile().isFollowing()).isEqualTo(true);
        });
    }

    @Test
    void 팔로우중이_아닌_유저의_프로필을_가져온다() {
        // given
        dbInitializer.유저들이_회원가입_돼있다();

        // when
        final ExtractableResponse<Response> extractableResponse = 로그인_상태로_GET_요청을_보낸다(
            JOHN, "/profiles/" + ALICE.getUsername());
        final ProfileApiResponse response = extractableResponse.as(ProfileApiResponse.class);

        // then
        assertAll(() -> {
            assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getProfile().getUsername()).isEqualTo(ALICE.getUsername());
            assertThat(response.getProfile().getBio()).isEqualTo(ALICE.getBio());
            assertThat(response.getProfile().getImage()).isEqualTo(ALICE.getImageUrl());
            assertThat(response.getProfile().isFollowing()).isEqualTo(false);
        });
    }

    @Test
    void 팔로우를_한다() {
        // given
        dbInitializer.유저들이_회원가입_돼있다();

        // when
        final ExtractableResponse<Response> extractableResponse = 로그인_상태로_POST_요청을_보낸다(
            JOHN, "/profiles/" + ALICE.getUsername() + "/follow", "");
        final ProfileApiResponse response = extractableResponse.as(ProfileApiResponse.class);

        // then
        assertAll(() -> {
            assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getProfile().getUsername()).isEqualTo(ALICE.getUsername());
            assertThat(response.getProfile().getBio()).isEqualTo(ALICE.getBio());
            assertThat(response.getProfile().getImage()).isEqualTo(ALICE.getImageUrl());
            assertThat(response.getProfile().isFollowing()).isEqualTo(true);
        });

    }

    @Test
    void 팔로우를_취소한다() {
        // given
        dbInitializer.JOHN이_ALICE를_팔로우한다();

        // when
        final ExtractableResponse<Response> extractableResponse = 로그인_상태로_POST_요청을_보낸다(
            JOHN, "/profiles/" + ALICE.getUsername() + "/unfollow", "");
        final ProfileApiResponse response = extractableResponse.as(ProfileApiResponse.class);

        // then
        assertAll(() -> {
            assertThat(extractableResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(response.getProfile().getUsername()).isEqualTo(ALICE.getUsername());
            assertThat(response.getProfile().getBio()).isEqualTo(ALICE.getBio());
            assertThat(response.getProfile().getImage()).isEqualTo(ALICE.getImageUrl());
            assertThat(response.getProfile().isFollowing()).isEqualTo(false);
        });

    }

}