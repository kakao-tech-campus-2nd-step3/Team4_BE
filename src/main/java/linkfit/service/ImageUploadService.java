package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.FAILED_UPLOAD_IMAGE;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_IMAGE;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
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
        if (profileImage == null) {
            return "https://nurspace-bucket.s3.ap-northeast-2.amazonaws.com/default_profile.jpg";
        }
        return uploadFile(profileImage);
    }

    public String saveImage(MultipartFile image) {
        if (image == null) {
            throw new ImageUploadException(NOT_FOUND_IMAGE);
        }
        return uploadFile(image);
    }

    private String uploadFile(MultipartFile file) {
        try {
            String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(awsProperties.s3().bucket(), key, file.getInputStream(), metadata);
            return String.format("https://%s.s3.%s.amazonaws.com/%s", awsProperties.s3().bucket(),
                awsProperties.region(), key);
        } catch (IOException e) {
            throw new ImageUploadException(FAILED_UPLOAD_IMAGE);
        }
    }
}