package real.world.security.authentication;

import static real.world.error.ErrorCode.FORMAT_INVALID;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import real.world.domain.user.dto.request.LoginRequest;
import real.world.error.exception.AuthenticationErrorCodeException;
import real.world.security.RootNameObjectMapper;

public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper mapper = new RootNameObjectMapper();

    public CustomUsernamePasswordAuthenticationFilter(String defaultFilterProcessesUrl,
        AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationErrorCodeException(FORMAT_INVALID.toString(), FORMAT_INVALID);
        }
        final UsernamePasswordAuthenticationToken authenticationToken;
        try {
            final LoginRequest loginRequest = mapper.readerFor(LoginRequest.class).withRootName("user").readValue(request.getInputStream());
            authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (IOException ioException) {
            throw new AuthenticationErrorCodeException(FORMAT_INVALID.toString(), FORMAT_INVALID);
        }
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}