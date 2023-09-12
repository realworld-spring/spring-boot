package real.world.security.service;

import static real.world.error.ErrorCode.USERID_NOT_EXIST;

import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.AuthenticationErrorCodeException;

@RequiredArgsConstructor
@Service
public class UserDetailsByIdService implements CustomUserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByPrincipal(String id) {
        final User user = userRepository.findById(Long.parseLong(id))
            .orElseThrow(
                () -> new AuthenticationErrorCodeException(USERID_NOT_EXIST));
        final Set<SimpleGrantedAuthority> authorities = Collections.singleton(
            new SimpleGrantedAuthority(user.getRole().getValue()));
        return new CustomUserDetails(user, authorities);
    }

}