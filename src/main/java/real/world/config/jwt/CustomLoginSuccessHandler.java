package real.world.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import real.world.domain.user.dto.response.LoginResponse;
import real.world.domain.user.entity.User;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${auth.header}")
    private String AUTH_HEADER;

    @Value("${auth.type}")
    private String TOKEN_TYPE;


    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication) throws IOException
    {
        final User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        final String token = JwtUtils.generateJwtToken(user);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);

        RootNameObjectMapper mapper = RootNameObjectMapper.of();
        response.getWriter().write(mapper.writeValueAsString(LoginResponse.of(user)));
        response.flushBuffer();
    }
}