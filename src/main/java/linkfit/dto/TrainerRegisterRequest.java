package linkfit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import linkfit.entity.Trainer;
import linkfit.status.TrainerGender;

public record TrainerRegisterRequest(
    @NotBlank @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 잘못되었습니다.") String email,
    @NotBlank (message = "비밀번호는 필수입니다.")String password,
    @NotBlank @NotBlank(message = "이름은 필수입니다.") String name,
    @NotNull TrainerGender gender) {

    public Trainer toEntity(String encodedPassword) {
        return new Trainer(email, encodedPassword, name(), gender);
    }
}
