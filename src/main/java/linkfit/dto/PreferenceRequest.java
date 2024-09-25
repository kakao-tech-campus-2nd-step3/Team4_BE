package linkfit.dto;

import linkfit.entity.Preference;
import linkfit.entity.SportsType;

public record PreferenceRequest(String gender, SportsType sportsType, int range, String goal) {

    public Preference toEntity() {
        return new Preference(gender, sportsType, range, goal);
    }
}
