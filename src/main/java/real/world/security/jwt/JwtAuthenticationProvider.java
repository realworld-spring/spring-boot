package real.world.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import real.world.error.exception.AuthenticationErrorCodeException;
import real.world.security.service.CustomUserDetailsService;
import real.world.security.support.JwtUtil;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication jwtAuthentication)
        throws AuthenticationErrorCodeException {
        final String jwt = jwtAuthentication.getPrincipal().toString();
        Authentication authentication = jwtUtil.getAuthentication(jwt);

        final String id = authentication.getPrincipal().toString();
        userDetailsService.loadUserByPrincipal(id);

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
