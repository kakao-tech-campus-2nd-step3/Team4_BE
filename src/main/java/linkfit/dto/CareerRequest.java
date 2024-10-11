package linkfit.dto;

import jakarta.validation.constraints.NotBlank;

public record CareerRequest(@NotBlank String career) {

}