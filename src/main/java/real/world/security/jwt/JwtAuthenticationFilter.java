package real.world.security.jwt;

import static real.world.error.ErrorCode.JWT_FORMAT_INVALID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import real.world.error.exception.AuthenticationErrorCodeException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTH_HEADER = "Authorization";

    public JwtAuthenticationFilter(
        RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        if(request.getHeader(AUTH_HEADER) == null) {
            throw new AuthenticationErrorCodeException(JWT_FORMAT_INVALID);
        }
        final String[] jwt = request.getHeader(AUTH_HEADER).split(" ");
        if(!jwt[0].equals("BEARER")) {
            throw new AuthenticationErrorCodeException(JWT_FORMAT_INVALID);
        }
        final Authentication authentication = new JwtAuthenticationToken(jwt[1]);
        return this.getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }

}
