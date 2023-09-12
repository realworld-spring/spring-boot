package real.world.security.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import real.world.error.ErrorCode;
import real.world.error.exception.AuthenticationErrorCodeException;
import real.world.security.service.CustomUserDetails;
import real.world.security.service.CustomUserDetailsService;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        final String email = token.getName();
        final String password = (String) token.getCredentials();

        final CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByPrincipal(email);
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new AuthenticationErrorCodeException(ErrorCode.WRONG_PASSWORD);
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getId(), password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
