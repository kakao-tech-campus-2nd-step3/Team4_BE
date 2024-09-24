package linkfit.service;

import java.io.IOException;
import java.util.UUID;

import linkfit.config.properties.AwsProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import linkfit.entity.Person;
import linkfit.exception.ImageUploadException;

@Service
@EnableConfigurationProperties(AwsProperties.class)
public class ImageUploadService {

    private final AmazonS3 amazonS3;

    private final AwsProperties awsProperties;

    public ImageUploadService(AmazonS3 amazonS3, AwsProperties awsProperties) {
        this.amazonS3 = amazonS3;
        this.awsProperties = awsProperties;
    }

    public <T extends Person> void saveProfileImage(T entity, MultipartFile profileImage) {
        entity.setProfileImageUrl("https://default-profile-url.com/default_profile.png");
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = uploadFile(profileImage);
            entity.setProfileImageUrl(imageUrl);
        }
    }

    private String uploadFile(MultipartFile file) {
        try {
            String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(awsProperties.s3().bucket(), key, file.getInputStream(), metadata);
            return String.format("https://%s.s3.%s.amazonaws.com/%s", awsProperties.s3().bucket(), awsProperties.region(), key);
        } catch (IOException e) {
            throw new ImageUploadException("Upload failed.");
        }
    }
}