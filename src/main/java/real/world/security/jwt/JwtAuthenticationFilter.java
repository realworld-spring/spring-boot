package real.world.security.jwt;

import static java.util.stream.Collectors.toList;
import static real.world.error.ErrorCode.JWT_FORMAT_INVALID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import real.world.error.exception.AuthenticationErrorCodeException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTH_HEADER = "Authorization";
    final RequestMatcher optionalRequestMatcher;

    public JwtAuthenticationFilter(
        RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager,
        String[] optionalPath) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.optionalRequestMatcher = createOptionalRequestMatcher(optionalPath);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        if(request.getHeader(AUTH_HEADER) == null) {
            throw new AuthenticationErrorCodeException(JWT_FORMAT_INVALID);
        }
        final String[] jwt = request.getHeader(AUTH_HEADER).split(" ");
        if(!jwt[0].equals("Token")) {
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

    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, AuthenticationException failed)
        throws IOException, ServletException {
        if(isOptionalPath(request)) {
            chain.doFilter(request, response);
            return;
        }
        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (!requiresAuthentication(request, response)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            Authentication authenticationResult = attemptAuthentication(request, response);
            if (authenticationResult == null) {
                return;
            }
            // Authentication success
            successfulAuthentication(request, response, chain, authenticationResult);
        }
        catch (InternalAuthenticationServiceException failed) {
            this.logger.error("An internal error occurred while trying to authenticate the user.", failed);
            unsuccessfulAuthentication(request, response, failed);
        }
        catch (AuthenticationException ex) {
            // Authentication failed
            unsuccessfulAuthentication(request, response, chain, ex);
        }
    }

    private RequestMatcher createOptionalRequestMatcher(String[] path) {
        final List<String> optionallist = Arrays.stream(path).toList();
        List<RequestMatcher> requestMatchers = optionallist.stream()
            .map(AntPathRequestMatcher::new)
            .collect(toList());
        return new OrRequestMatcher(requestMatchers);
    }

    private boolean isOptionalPath(HttpServletRequest request) {
        return optionalRequestMatcher.matches(request);
    }

}
