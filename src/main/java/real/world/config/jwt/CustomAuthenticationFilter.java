package real.world.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import real.world.domain.user.entity.User;
import real.world.error.ErrorCode;
import real.world.error.exception.AuthenticationErrorCodeException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) {
        final UsernamePasswordAuthenticationToken authenticationToken;
        try {
            RootNameObjectMapper mapper = RootNameObjectMapper.of();
            final User user = mapper.readerFor(User.class).withRootName("user").readValue(request.getInputStream());
            authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(),
                user.getPassword());
        } catch (IOException ioException) {
            throw AuthenticationErrorCodeException.of(ErrorCode.FORMAT_INVALID);
        }
        setDetails(request, authenticationToken);
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

}