package real.world.e2e;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

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

}
