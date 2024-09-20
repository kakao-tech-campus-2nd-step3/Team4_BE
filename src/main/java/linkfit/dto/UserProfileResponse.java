package linkfit.dto;

public class UserProfileResponse {

    private String name;
    private String local;
    private String profileImageUrl;

    public UserProfileResponse(String name, String local, String profileImageUrl) {
        this.name = name;
        this.local = local;
        this.profileImageUrl = profileImageUrl;
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
