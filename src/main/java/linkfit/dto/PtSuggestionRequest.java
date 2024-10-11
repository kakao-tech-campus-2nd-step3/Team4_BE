package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record PtSuggestionRequest(@NotNull Long userId, @NotNull @Positive int totalCount,
                                  @NotNull @PositiveOrZero int price) {

}