package linkfit.dto;

import linkfit.entity.Preference;
import linkfit.entity.Sports;
import linkfit.entity.User;
import linkfit.status.TrainerGender;

public record PreferenceRequest(Long sportsId, TrainerGender gender, int range, String goal) {

    public Preference toEntity(User user, Sports sports) {
        return new Preference(user, sports, gender, range, goal);
    }
}