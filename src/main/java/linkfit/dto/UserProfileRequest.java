package linkfit.dto;

public class UserProfileRequest {

    private String local;
    private String name;

    public UserProfileRequest(String local, String name) {
        this.local = local;
        this.name = name;
    }

    public String getLocal() {
        return local;
    }

    public String getName() {
        return name;
    }
}
