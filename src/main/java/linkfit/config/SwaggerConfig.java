package linkfit.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "jwt";

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(
            SECURITY_SCHEME_NAME);
        Components components = new Components()
            .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
            );
        return new OpenAPI()
            .components(components)
            .info(apiInfo())
            .addSecurityItem(securityRequirement);
    }

    private Info apiInfo() {
        return new Info()
            .title("LINKFIT API") // API의 제목
            .description("PT 매칭 서비스 LINKFIT의 API입니다.") // API에 대한 설명
            .version("1.0.0"); // API의 버전
    }
}
