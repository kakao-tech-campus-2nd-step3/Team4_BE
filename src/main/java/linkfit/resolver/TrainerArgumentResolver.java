package linkfit.resolver;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import linkfit.annotation.LoginTrainer;
import linkfit.entity.Trainer;
import linkfit.service.TrainerService;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class TrainerArgumentResolver implements HandlerMethodArgumentResolver {

    TrainerService trainerService;


    private final String secret = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";


    public TrainerArgumentResolver(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginTrainer.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(
            HttpServletRequest.class);
        if (httpServletRequest == null) {
            throw new IllegalStateException("token null");
        }
        String token = retrieveToken(httpServletRequest);

        if (!validToken(token)) {
            throw new IllegalStateException("token not valid");
        }

        return resolveTrainer(token);
    }

    private String retrieveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    //JWT 토큰 유효성 검증
    private boolean validToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token).getBody();
            return true;

        } catch (Exception e) {
            return false;
        }
    }


    //토큰 파싱 정보로 Trainer Entity 반환
    private Trainer resolveTrainer(String token) {

        String email = Jwts.parser()
            .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
            .build()
            .parseClaimsJws(token).getBody().get("email", String.class);

        return trainerService.findByEmail(email);
    }

}
