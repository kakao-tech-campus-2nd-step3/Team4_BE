package linkfit.dto;

import linkfit.entity.BodyInfo;
import linkfit.entity.Preference;
import linkfit.entity.Sports;

public record PreferenceRequest(String gender, Long sportsId, Integer range, String goal) {

}