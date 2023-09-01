package real.world.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import real.world.domain.user.entity.User;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${auth.header}")
    private String AUTH_HEADER;

    @Value("${auth.type}")
    private String TOKEN_TYPE;

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication)
    {
        final User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        final String token = TokenUtils.generateJwtToken(user);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);
    }

}