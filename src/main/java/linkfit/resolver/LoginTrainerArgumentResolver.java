package linkfit.resolver;

import static linkfit.util.JwtUtil.AUTHORIZATION_HEADER;
import static linkfit.util.JwtUtil.BEARER_PREFIX;

import java.util.Objects;
import linkfit.annotation.LoginTrainer;
import linkfit.exception.InvalidTokenException;
import linkfit.exception.PermissionException;
import linkfit.util.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginTrainerArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    public LoginTrainerArgumentResolver(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginTrainer.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(AUTHORIZATION_HEADER);
        if (token == null) {
            throw new PermissionException("not.found.token");
        }
        String processedToken = token.replace(BEARER_PREFIX, "");
        if (!jwtUtil.isValidToken(processedToken)) {
            throw new InvalidTokenException("invalid.token");
        }
        return jwtUtil.parseToken(Objects.requireNonNull(token));
    }
}
