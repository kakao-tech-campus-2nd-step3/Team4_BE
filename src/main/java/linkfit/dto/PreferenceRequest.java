package linkfit.dto;

import linkfit.entity.Preference;
import linkfit.entity.SportsType;

public class PreferenceRequest {

	private String gender;
	private SportsType sprotsType;
	private int range;
	private String goal;
	
	public SportsType getSportsType() {
		return sprotsType;
	}
	
	public Preference toEntity() {
		Preference preference = new Preference(gender, sprotsType, range, goal);
		return preference;
	}
}
