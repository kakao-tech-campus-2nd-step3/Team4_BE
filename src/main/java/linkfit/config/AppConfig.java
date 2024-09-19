package linkfit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.service.AuthService;
import linkfit.service.ImageUploadService;
import linkfit.service.TrainerService;
import linkfit.service.UserService;

@Configuration
public class AppConfig {

	@Bean
    public AuthService<User> userAuthService(UserService userService, ImageUploadService imageUploadService) {
        return new AuthService<>(userService, imageUploadService);
    }

    @Bean
    public AuthService<Trainer> trainerAuthService(TrainerService trainerService, ImageUploadService imageUploadService) {
        return new AuthService<>(trainerService, imageUploadService);
    }
}
