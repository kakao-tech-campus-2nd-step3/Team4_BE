package linkfit.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws")
public record AwsProperties(Credentials credentials, Region region, S3 s3) {

	public AwsProperties(Credentials credentials, Region region, S3 s3) {
        this.credentials = credentials;
        this.region = setDefaultRegion(region);
        this.s3 = s3;
    }
	
	private Region setDefaultRegion(Region region) {
        if (region == null) {
            return new Region("ap-northeast-2");
        }
        return region;
    }
	
    public record Credentials(String accessKey, String secretKey) {

    }

    public record Region(String value) {

    }

    public record S3(String bucket) {

    }
}
