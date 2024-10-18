package linkfit.config;

import jakarta.annotation.PostConstruct;
import linkfit.component.DefaultImageProvider;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultImageUrlConfig {

    private final DefaultImageProvider defaultImageProvider;

    public DefaultImageUrlConfig(DefaultImageProvider defaultImageProvider) {
        this.defaultImageProvider = defaultImageProvider;
    }

    @PostConstruct
    public void init() {
        Trainer.setDefaultImageProvider(defaultImageProvider);
        User.setDefaultImageProvider(defaultImageProvider);
    }
}
