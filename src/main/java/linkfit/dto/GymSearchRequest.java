package linkfit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GymSearchRequest(@NotBlank @Size(min = 2) String keyword) {

}