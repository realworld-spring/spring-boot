package real.world.security;

import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.ErrorCode;
import real.world.error.exception.AuthenticationErrorCodeException;
import real.world.security.authentication.CustomUserDetails;

@RequiredArgsConstructor
@Service
public class UserDetailsByIdService implements CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByPrincipal(String id) {
        User user = userRepository.findById(Long.parseLong(id))
            .orElseThrow(
                () -> new AuthenticationErrorCodeException(ErrorCode.USERID_ALREADY_EXIST.toString(),
                    ErrorCode.USERID_ALREADY_EXIST));
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(
            new SimpleGrantedAuthority(user.getRole().getValue()));
        return new CustomUserDetails(user, authorities);
    }

}