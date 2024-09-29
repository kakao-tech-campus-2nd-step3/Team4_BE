package linkfit.dto;

import static linkfit.exception.GlobalExceptionHandler.INVALID_EMAIL_FORMAT;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoginRequest(@NotNull @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = INVALID_EMAIL_FORMAT)String email, @NotNull String password) {
}