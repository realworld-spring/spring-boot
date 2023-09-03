package real.world.config.jwt;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import real.world.domain.user.repository.UserRepository;
import real.world.error.ErrorCode;
import real.world.error.exception.AuthenticationErrorCodeException;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
            .map(user -> new CustomUserDetails(
                user,
                Collections.singleton(new SimpleGrantedAuthority(
                    user.getRole().getValue()))))
            .orElseThrow(() -> AuthenticationErrorCodeException.of(ErrorCode.USERNAME_NOT_EXIST));
    }

}