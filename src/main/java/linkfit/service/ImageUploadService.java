package linkfit.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

import linkfit.config.properties.AwsProperties;
import linkfit.exception.ImageUploadException;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@EnableConfigurationProperties(AwsProperties.class)
public class ImageUploadService {

	private final AmazonS3 amazonS3;
	private final AwsProperties awsProperties;

	public ImageUploadService(AmazonS3 amazonS3, AwsProperties awsProperties) {
		this.amazonS3 = amazonS3;
		this.awsProperties = awsProperties;
	}

    public String uploadProfileImage(MultipartFile profileImage) {
        return uploadFile(profileImage);
    }

	public String saveImage(MultipartFile image) {
		if (image == null) {
			throw new ImageUploadException("not.found.image");
		}
		return uploadFile(image);
	}

	private String uploadFile(MultipartFile file) {
		try {
			String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
			ObjectMetadata metadata = createObjectMetadata(file);

			amazonS3.putObject(new PutObjectRequest(awsProperties.s3().bucket(), key, file.getInputStream(), metadata));

			return String.format("https://%s.s3.%s.amazonaws.com/%s", awsProperties.s3().bucket(),
					awsProperties.region(), key);
		} catch (IOException e) {
			throw new ImageUploadException("failed.upload.image");
		}
	}

	private ObjectMetadata createObjectMetadata(MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		String contentType = file.getContentType();

		if (contentType == null || contentType.isBlank()) {
			contentType = "multipart/form-data";
		}
		metadata.setContentType(contentType);
		metadata.setContentLength(file.getSize());
		return metadata;
	}
}