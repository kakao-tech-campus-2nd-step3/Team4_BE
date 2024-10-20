package linkfit.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultImageProvider {

    @Value("${default-image-url}")
    private String defaultImageUrl;

    public String getDefaultImageUrl() {
        return defaultImageUrl;
    }
}
