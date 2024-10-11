package linkfit.dto;

import jakarta.validation.constraints.NotBlank;
import linkfit.entity.Gym;

public record GymRegisterRequest(@NotBlank String name, @NotBlank String location) {

    public Gym toEntity() {
        return new Gym(name, location);
    }
}