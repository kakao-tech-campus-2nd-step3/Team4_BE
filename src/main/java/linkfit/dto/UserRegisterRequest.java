package linkfit.dto;

import linkfit.entity.User;

public class UserRegisterRequest extends RegisterRequest<User> {

    private String local;

    public String getLocal() {
        return local;
    }

    public User toEntity() {
        return new User(getEmail(), getPassword(), getName(), local);
    }
}
