package linkfit.dto;

import linkfit.entity.Preference;
import linkfit.entity.SprotsType;

public class PreferenceRequest {

	private String gender;
	private SprotsType sprotsType;
	private int range;
	private String goal;
	
	public Preference toEntity() {
		Preference preference = new Preference(gender, sprotsType, range, goal);
		return preference;
	}
}
