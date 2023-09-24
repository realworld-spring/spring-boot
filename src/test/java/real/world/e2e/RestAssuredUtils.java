package real.world.e2e;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import real.world.domain.user.dto.request.LoginRequest;
import real.world.fixture.UserFixtures;

public class RestAssuredUtils {

    public static ExtractableResponse<Response> GET_요청을_보낸다(String url) {
        return RestAssured
            .given().log().all()
            .when().get(url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> POST_요청을_보낸다(String url, Object body) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE).body(body)
            .when().post(url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인_상태로_GET_요청을_보낸다(UserFixtures user, String url) {
        final String token = 로그인_요청을_보낸다(user);
        return RestAssured
            .given().log().all()
            .header("Authorization", token)
            .when().get(url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인_상태로_POST_요청을_보낸다(UserFixtures user, String url,
        Object body) {
        final String token = 로그인_요청을_보낸다(user);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE).body(body)
            .header("Authorization", token)
            .when().post(url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인_상태로_PUT_요청을_보낸다(UserFixtures user, String url,
        Object body) {
        final String token = 로그인_요청을_보낸다(user);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE).body(body)
            .header("Authorization", token)
            .when().put(url)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인_상태로_DELETE_요청을_보낸다(UserFixtures user, String url) {
        final String token = 로그인_요청을_보낸다(user);
        return RestAssured
            .given().log().all()
            .header("Authorization", token)
            .when().delete(url)
            .then().log().all()
            .extract();
    }

    private static String 로그인_요청을_보낸다(UserFixtures user) {
        final LoginRequest request = user.로그인을_한다();
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE).body(request)
            .when().post("/users/login")
            .then().extract().header("Authorization");
    }

}
