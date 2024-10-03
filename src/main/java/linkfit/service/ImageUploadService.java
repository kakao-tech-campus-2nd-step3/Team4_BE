package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.FAILED_UPLOAD_IMAGE;

import java.io.IOException;
import java.util.UUID;

import linkfit.config.properties.AwsProperties;

import linkfit.entity.BodyInfo;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

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

    public void saveUserProfileImage(User user, MultipartFile profileImage) {
        user.setProfileImageUrl("https://default-profile-url.com/default_profile.png");
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = uploadFile(profileImage);
            user.setProfileImageUrl(imageUrl);
        }
    }

    public void saveTrainerProfileImage(Trainer trainer, MultipartFile profileImage) {
        trainer.setProfileImageUrl("https://default-profile-url.com/default_profile.png");
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = uploadFile(profileImage);
            trainer.setProfileImageUrl(imageUrl);
        }
    }

    public BodyInfo saveInbodyImage(User user, MultipartFile inbodyImage) {
        if (inbodyImage == null) {
            throw new ImageUploadException("유효하지 않은 Inbody Image");
        }
        String imageUrl = uploadFile(inbodyImage);
        return new BodyInfo(user, imageUrl);

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