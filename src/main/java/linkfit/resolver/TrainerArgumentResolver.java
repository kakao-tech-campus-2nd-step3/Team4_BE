package linkfit.resolver;

import jakarta.servlet.http.HttpServletRequest;
import linkfit.annotation.LoginTrainer;
import linkfit.entity.Trainer;
import linkfit.service.TrainerService;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class TrainerArgumentResolver implements HandlerMethodArgumentResolver {

    TrainerService trainerService;

    public TrainerArgumentResolver(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter){
        return parameter.hasParameterAnnotation(LoginTrainer.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        //Header check
        if(httpServletRequest !=null){
            String token = httpServletRequest.getHeader("Authorization");
        }

        //jwtUtil 활용 토큰 파싱

        return null;
    }




    //토큰 파싱 정보로 Trainer Entity 반환
    private Trainer resolveTrainer(String email){
        return trainerService.findByEmail(email);
    }

}
