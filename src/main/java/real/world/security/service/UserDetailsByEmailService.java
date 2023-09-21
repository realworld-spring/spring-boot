package real.world.security.service;

import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.ErrorCode;
import real.world.error.exception.AuthenticationErrorCodeException;

@RequiredArgsConstructor
@Service
public class UserDetailsByEmailService implements CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByPrincipal(String email) throws AuthenticationException {
        final User user = userRepository.findByEmail(email)
            .orElseThrow(
                () -> new AuthenticationErrorCodeException(ErrorCode.AUTH_USERNAME_NOT_EXIST));
        final Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(user.getRole().getValue()));
        return new CustomUserDetails(user, authorities);
    }

}