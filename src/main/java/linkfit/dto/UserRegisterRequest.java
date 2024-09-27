package linkfit.dto;

import linkfit.entity.User;

public class UserRegisterRequest extends RegisterRequest<User> {

    private final String local;

    public UserRegisterRequest(String email, String password, String passwordConfirm, String name, String local) {
        super(email, password, passwordConfirm, name);
        this.local = local;
    }

    public String getLocal() {
        return local;
    }

    @Override
    public User toEntity() {
        return new User(getEmail(), getPassword(), getName(), local);
    }
}