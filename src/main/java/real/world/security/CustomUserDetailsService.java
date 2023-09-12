package real.world.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {

    UserDetails loadUserByPrincipal(String principal) throws AuthenticationException;

}