package real.world.security;

import static java.util.stream.Collectors.toList;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class NorRequestMatcher implements RequestMatcher {

    private final List<RequestMatcher> requestMatchers;

    public NorRequestMatcher(String[] PATH) {
        final List<String> list = Arrays.stream(PATH).toList();
        this.requestMatchers = list.stream()
            .map(AntPathRequestMatcher::new)
            .collect(toList());
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        for (RequestMatcher matcher : this.requestMatchers) {
            if (matcher.matches(request)) {
                return false;
            }
        }
        return true;
    }

}
