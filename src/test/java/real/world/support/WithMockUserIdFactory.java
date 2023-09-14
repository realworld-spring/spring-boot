package real.world.support;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import real.world.security.jwt.JwtAuthenticationToken;

public class WithMockUserIdFactory implements WithSecurityContextFactory<WithMockUserId> {

    @Override
    public SecurityContext createSecurityContext(WithMockUserId annotation) {
        final String id = String.valueOf(annotation.user().getId());
        final Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(annotation.role().toString())
        );

        final Authentication authentication = new JwtAuthenticationToken(id, authorities);
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

}
