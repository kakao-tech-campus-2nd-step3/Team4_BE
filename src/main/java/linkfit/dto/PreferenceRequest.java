package linkfit.dto;

import linkfit.entity.BodyInfo;
import linkfit.entity.Preference;
import linkfit.entity.Sports;

public record PreferenceRequest(String gender, Long sportsId, Integer range, String goal) {


    public Preference toEntity(BodyInfo bodyInfo, Sports sports){
        return new Preference(gender, sports, bodyInfo, range,
                goal());
    }
}