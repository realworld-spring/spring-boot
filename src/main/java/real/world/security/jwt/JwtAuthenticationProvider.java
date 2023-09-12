package real.world.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import real.world.security.CustomUserDetailsService;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;

    private final JwtUtil jwtUtil;

    @Override
    public Authentication authenticate(Authentication jwtAuthentication)
        throws AuthenticationException {
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
