package linkfit.config;

import java.util.List;
import linkfit.resolver.LoginArgumentResolver;
import linkfit.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    @Value("${jwt.master.id}")
    private Long masterId;

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil(expirationTime, masterId);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(jwtUtil()));
    }
}