package real.world.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import real.world.error.ErrorResponse;
import real.world.error.exception.AuthenticationErrorCodeException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper mapper = new RootNameObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {
        AuthenticationErrorCodeException authException = (AuthenticationErrorCodeException) exception;
        response.getWriter().write(mapper.writeValueAsString(ErrorResponse.of(authException.getErrorCode())));
        response.flushBuffer();
    }
}
