package real.world.security;

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
import real.world.error.exception.JwtInvalidException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtUtil jwtUtil;

    private final String AUTH_HEADER = "Authorization";

    public JwtAuthenticationFilter(
        RequestMatcher requiresAuthenticationRequestMatcher,
        AuthenticationManager authenticationManager,
        JwtUtil jwtUtil) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final Authentication authentication;
        try {
            final String jwt = jwtUtil.getJwtFromHeader(request.getHeader(AUTH_HEADER));
            authentication = (jwtUtil.isValidJwt(jwt)) ? jwtUtil.getAuthentication(jwt) : null;
        } catch (Exception e) {
            throw new JwtInvalidException();
        }
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
