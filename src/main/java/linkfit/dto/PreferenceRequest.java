package linkfit.dto;

import linkfit.entity.BodyInfo;
import linkfit.entity.Preference;
import linkfit.entity.Sports;

public record PreferenceRequest(String gender, Sports sports, int range, String goal) {

    public Preference toEntity(BodyInfo bodyInfo) {
        return new Preference(gender, sports, bodyInfo, range, goal);
    }
}