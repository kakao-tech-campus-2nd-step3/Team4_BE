package linkfit.dto;

import jakarta.validation.constraints.NotBlank;
import linkfit.entity.Sports;

public record SportsRequest(@NotBlank String name) {

    public SportsRequest() {
        this("");
    }

    public Sports toEntity() {
        return new Sports(name);
    }
}