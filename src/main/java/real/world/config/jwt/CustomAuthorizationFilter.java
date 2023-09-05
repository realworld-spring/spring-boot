package real.world.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import real.world.error.ErrorResponse;
import real.world.error.exception.JwtInvalidException;


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
                if (JwtUtils.isValidJwt(jwt)) {
                    Authentication authentication = JwtUtils.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            RootNameObjectMapper mapper = RootNameObjectMapper.of();
            response.getWriter().write(mapper.writeValueAsString(ErrorResponse.of(new JwtInvalidException())));
            response.flushBuffer();
        }
    }

}