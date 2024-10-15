package linkfit.resolver;

import static linkfit.util.JwtUtil.AUTHORIZATION_HEADER;
import static linkfit.util.JwtUtil.BEARER_PREFIX;
import static linkfit.util.JwtUtil.BEARER_PREFIX_LENGTH;

import linkfit.annotation.Login;
import linkfit.exception.PermissionException;
import linkfit.util.JwtUtil;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;

    public LoginArgumentResolver(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Login.class);
    }

    @Override
    public Long resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader(AUTHORIZATION_HEADER);
        validateTokenPresence(token);
        validateTokenPrefix(token);
        String processedToken = token.substring(BEARER_PREFIX_LENGTH);
        return jwtUtil.parseToken(processedToken);
    }

    private void validateTokenPresence(String token) {
        if (token == null || token.isEmpty()) {
            throw new PermissionException("not.found.token");
        }
    }

    private void validateTokenPrefix(String token) {
        if (!token.startsWith(BEARER_PREFIX)) {
            throw new PermissionException("not.match.prefix");
        }
    }
}
