package linkfit.dto;

import jakarta.validation.constraints.NotBlank;

public record UserProfileRequest(@NotBlank String location, @NotBlank String name) {

}