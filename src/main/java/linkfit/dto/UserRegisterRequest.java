package linkfit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import linkfit.entity.User;

public record UserRegisterRequest(
    @NotBlank @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$") String email,
    @NotBlank String password, @NotBlank String name, @NotBlank String location) {

    public User toEntity(String encodedPassword) {
        return new User(email, encodedPassword, name, location);

    }
}