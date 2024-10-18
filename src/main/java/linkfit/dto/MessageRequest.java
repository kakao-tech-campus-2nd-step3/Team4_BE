package linkfit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import linkfit.status.Role;

public record MessageRequest(@NotNull Long roomId, @NotBlank String content, @NotNull Role sender) {

}