package linkfit.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import linkfit.entity.PersonEntity;
import linkfit.exception.ImageUploadException;

@Service
public class ImageUploadService {

	private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;
    
    public ImageUploadService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }
    
    public <T extends PersonEntity> void profileImageSave(T entity, MultipartFile profileImage) {
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
            amazonS3.putObject(bucket, key, file.getInputStream(), metadata);
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, key);
        } catch (IOException e) {
            throw new ImageUploadException("Upload failed.");
        }
    }
}
