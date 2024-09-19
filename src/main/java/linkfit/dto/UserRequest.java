package linkfit.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserRequest {

    private MultipartFile profileImage;
    private String local;
    private String goal;

    UserRequest(MultipartFile profileImageUrl, String local, String goal) {
        this.profileImage = profileImage;
        this.local = local;
        this.goal = goal;
    }

    public MultipartFile getProfileImage() {
        return profileImage;
    }

    public String getLocal() {
        return local;
    }

    public String getGoal() {
        return goal;
    }
}
