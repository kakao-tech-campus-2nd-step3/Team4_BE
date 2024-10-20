package linkfit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import linkfit.entity.Trainer;
import linkfit.status.TrainerGender;

public record TrainerRegisterRequest(
    @NotBlank @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$") String email,
    @NotBlank String password, @NotBlank String name,
    @Pattern(regexp = "MALE|FEMALE") TrainerGender gender) {

    public Trainer toEntity(String encodedPassword) {
        return new Trainer(email, encodedPassword, name(), gender);
    }
}
