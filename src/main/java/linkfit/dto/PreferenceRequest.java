package linkfit.dto;

import linkfit.entity.BodyInfo;
import linkfit.entity.Preference;
import linkfit.entity.Sports;
import linkfit.entity.User;
import linkfit.status.TrainerGender;

public record PreferenceRequest(Long sportsId, Long bodyInfoId, TrainerGender gender, int range, String goal) {

    public Preference toEntity(User user, BodyInfo bodyInfo, Sports sports) {
        return new Preference(user, bodyInfo, sports, gender, range, goal);
    }
}