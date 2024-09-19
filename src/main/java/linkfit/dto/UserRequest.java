package linkfit.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserRequest {

    private MultipartFile profileImageUrl;
    private String local;
    private String goal;

    UserRequest(MultipartFile profileImageUrl, String local, String goal) {
        this.profileImageUrl = profileImageUrl;
        this.local = local;
        this.goal = goal;
    }

    public MultipartFile getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getLocal() {
        return local;
    }

    public String getGoal() {
        return goal;
    }
}
