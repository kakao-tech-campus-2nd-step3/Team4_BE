package linkfit.dto;

import linkfit.entity.User;

public class UserRegisterRequest extends RegisterRequest<User> {

    private final String location;

    protected UserRegisterRequest() {
        super();
        this.location = "";
    }

    public UserRegisterRequest(String email, String password, String name, String location) {
        super(email, password, name);
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public User toEntity() {
        return new User(getEmail(), getPassword(), getName(), location);
    }
}