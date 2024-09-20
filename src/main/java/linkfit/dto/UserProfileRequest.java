package linkfit.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserProfileRequest {

    private MultipartFile profileImage;
    private String local;
    private String name;

    UserProfileRequest(MultipartFile profileImage, String local, String name) {
        this.profileImage = profileImage;
        this.local = local;
        this.name = name;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public String getLocal() {
        return local;
    }

    public String getName() {
        return name;
    }
}
