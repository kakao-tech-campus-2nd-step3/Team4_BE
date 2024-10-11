package linkfit.dto;

import jakarta.validation.constraints.NotBlank;

public record GymDescriptionRequest(@NotBlank String description) {

}