package real.world.config.jwt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import real.world.error.ErrorResponse;
import real.world.error.exception.AuthenticationErrorCodeException;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    public ObjectMapper userMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        return mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {

        AuthenticationErrorCodeException authException = (AuthenticationErrorCodeException) exception;
        response.getWriter().write(userMapper().writeValueAsString(ErrorResponse.of(authException.getErrorCode())));
        response.flushBuffer();
    }
}
