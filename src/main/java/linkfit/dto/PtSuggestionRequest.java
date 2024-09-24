package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record PtSuggestionRequest(@NotNull Long userId, @Positive int totalCount, @PositiveOrZero int price) {}
