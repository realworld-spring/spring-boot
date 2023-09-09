package real.world.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    public JwtAuthenticationProvider(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication authenticate(Authentication jwtAuthentication)
        throws AuthenticationException {
        final String jwt = jwtAuthentication.getPrincipal().toString();
        Authentication authentication = jwtUtil.getAuthentication(jwt);

        final String id = authentication.getPrincipal().toString();
        userDetailsService.loadUserByUsername(id);

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtAuthenticationToken.class);
    }
}
