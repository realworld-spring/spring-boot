package real.world.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import real.world.error.ErrorResponse;
import real.world.error.exception.AuthenticationErrorCodeException;

public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {

        RootNameObjectMapper mapper = RootNameObjectMapper.of();
        AuthenticationErrorCodeException authException = (AuthenticationErrorCodeException) exception;
        response.getWriter().write(mapper.writeValueAsString(ErrorResponse.of(authException.getErrorCode())));
        response.flushBuffer();
    }
}
