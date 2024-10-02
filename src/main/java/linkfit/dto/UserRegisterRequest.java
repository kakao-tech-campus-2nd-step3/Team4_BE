package linkfit.dto;

import linkfit.entity.User;

public record UserRegisterRequest(String email, String password, String name, String location) {

    public User toEntity() {
        return new User(email, password, name, location);
    }
}