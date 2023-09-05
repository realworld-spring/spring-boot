package real.world.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


public class CustomAuthorizationFilter extends OncePerRequestFilter {

//    @Value("${auth.header}")
    private final String AUTH_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = request.getHeader(AUTH_HEADER);
            if (header != null) {
                String jwt = JwtUtils.getJwtFromHeader(header);
                if(JwtUtils.isValidJwt(jwt)) {
                    Authentication authentication = JwtUtils.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            throw e;
        }

        filterChain.doFilter(request, response);
    }

}