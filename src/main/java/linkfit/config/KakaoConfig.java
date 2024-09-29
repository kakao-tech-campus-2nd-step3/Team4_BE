package linkfit.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import linkfit.config.properties.KakaoProperties;

@Configuration
@EnableConfigurationProperties(KakaoProperties.class)
public class KakaoConfig {
}