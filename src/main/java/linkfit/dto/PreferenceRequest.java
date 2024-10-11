package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import linkfit.entity.BodyInfo;
import linkfit.entity.Preference;
import linkfit.entity.Sports;
import linkfit.entity.User;
import linkfit.status.TrainerGender;

public record PreferenceRequest(@NotNull Long sportsId, @NotNull Long bodyInfoId,
                                TrainerGender gender, @NotNull @Positive int range, String goal) {

    public Preference toEntity(User user, BodyInfo bodyInfo, Sports sports) {
        return new Preference(user, bodyInfo, sports, gender, range, goal);
    }
}