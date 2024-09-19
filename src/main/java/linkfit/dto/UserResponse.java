package linkfit.dto;

import linkfit.entity.User;

public class UserResponse {

    private String name;
    private String local;
    private String profileImageUrl;

    public UserResponse(User user) {
        this.name = user.getName();
        this.local = user.getLocal();
        this.profileImageUrl = user.getProfileImageUrl();
    }

    public String getName() {
        return name;
    }

    public String getLocal() {
        return local;
    }

    public String profileImageUrl() {
        return profileImageUrl;
    }

}
