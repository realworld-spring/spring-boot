package real.world.domain.global;

import jakarta.annotation.Nonnull;
import java.util.regex.Pattern;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import real.world.error.exception.RequestInvalidException;

public class PageArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int MAX_LIMIT = 30;
    private static final Pattern NUMBER = Pattern.compile("^[0-9]*$");


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Page.class);
    }

    @Override
    public Object resolveArgument(@Nonnull MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        @Nonnull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String offsetArgument = webRequest.getParameter("offset");
        final String limitArgument = webRequest.getParameter("limit");
        return new Page(verifyAndConvertOffset(offsetArgument),
            verifyAndConvertLimit(limitArgument));
    }

    private int verifyAndConvertOffset(String offset) {
        if (offset == null || !NUMBER.matcher(offset).matches()) {
            throw new RequestInvalidException();
        }
        return Integer.parseInt(offset);
    }

    private int verifyAndConvertLimit(String limit) {
        if (limit == null || !NUMBER.matcher(limit).matches()) {
            throw new RequestInvalidException();
        }
        final int parsedLimit = Integer.parseInt(limit);
        if (parsedLimit > MAX_LIMIT) {
            throw new RequestInvalidException();
        }
        return parsedLimit;
    }

}
