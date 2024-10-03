package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import linkfit.entity.Gym;

public record GymRegisterRequest(@NotNull String name, @NotNull String location) {

    public Gym toEntity() {
        return new Gym(name, location);
    }
}