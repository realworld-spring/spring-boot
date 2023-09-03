package real.world.config.jwt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
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

    public ObjectMapper userMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
        return mapper;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication) throws ServletException, IOException
    {
        final User user = ((CustomUserDetails) authentication.getPrincipal()).getUser();
        final String token = TokenUtils.generateJwtToken(user);
        response.addHeader(AUTH_HEADER, TOKEN_TYPE + " " + token);

        response.getWriter().write(userMapper().writeValueAsString(LoginResponse.of(user)));
        response.flushBuffer();
    }
}