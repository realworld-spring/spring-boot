package real.world.security.jwt;

import static real.world.error.ErrorCode.JWT_FORMAT_INVALID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import real.world.domain.user.entity.UserRole;
import real.world.error.exception.AuthenticationErrorCodeException;
import real.world.security.support.OptionalRequest;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTH_HEADER = "Authorization";

    final RequestMatcher optionalRequestMatcher;

    public JwtAuthenticationFilter(
        RequestMatcher requiresAuthenticationRequestMatcher, AuthenticationManager authenticationManager,
        OptionalRequest[] optionalRequests) {
        super(requiresAuthenticationRequestMatcher, authenticationManager);
        this.optionalRequestMatcher = createOptionalRequestMatcher(optionalRequests);
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
        if(isOptionalRequest(request)) {
            final Authentication authResult = createAnonymousAuth();
            SecurityContextHolder.getContext().setAuthentication(authResult);
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

    private RequestMatcher createOptionalRequestMatcher(OptionalRequest[] optionalRequests) {
        final List<RequestMatcher> requestMatchers = new ArrayList<>();
        for (OptionalRequest optionalRequest : optionalRequests) {
            final String path = optionalRequest.getPath();
            for (String method : optionalRequest.getMethods()) {
                requestMatchers.add(new AntPathRequestMatcher(path, method));
            }
        }
        return new OrRequestMatcher(requestMatchers);
    }

    private boolean isOptionalRequest(HttpServletRequest request) {
        return optionalRequestMatcher.matches(request);
    }

    private Authentication createAnonymousAuth() {
        final Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_ANONYMOUS.getValue()));
        return new JwtAuthenticationToken("0", authorities);
    }

}
