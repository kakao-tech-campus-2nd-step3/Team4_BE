package linkfit.config;

import linkfit.config.properties.AwsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
public class AmazonS3ClientConfig {

	private final AwsProperties awsProperties;

	public AmazonS3ClientConfig(AwsProperties awsProperties) {
		this.awsProperties = awsProperties;
	}

	@Bean
	public AmazonS3 s3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(awsProperties.credentials().accessKey(),
				awsProperties.credentials().secretKey());
		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.fromName(awsProperties.region().statics())).build();
	}
}
