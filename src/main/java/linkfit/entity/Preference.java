package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import linkfit.dto.PreferenceResponse;

@Entity
@Table(name = "Preferences")
public class Preference {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "sports_type_id", nullable = false)
	private SportsType sportsType;
	
	@OneToOne
	@JoinColumn(name = "user_body_info_id", nullable = false)
	private UserBodyInfo userBodyInfo;
	
	@Column(nullable = false)
	private String gender;
	
	@Column(nullable = false)
	private int range;
	
	@Column(nullable = false)
	private String goal;
	
	public UserBodyInfo getUserBodyInfo() {
		return userBodyInfo;
	}
	
	public int getRange() {
		return range;
	}
	
	public void setSportsType(SportsType sportsType) {
		this.sportsType = sportsType;
	}
	
	public void setUserBodyInfo(UserBodyInfo userBodyInfo) {
		this.userBodyInfo = userBodyInfo;
	}
	
	public Preference() {}
	
	public Preference(String gender, SportsType sportsType, int range, String goal) {
		this.gender = gender;
		this.sportsType = sportsType;
		this.range = range;
		this.goal = goal;
	}
	
	public PreferenceResponse toDto() {
		PreferenceResponse preferenceResponse = new PreferenceResponse(
				userBodyInfo.getUser().getId(), userBodyInfo.getUser().getName(),
				userBodyInfo.getInbodyImageUrl(), goal, userBodyInfo.getUser().getProfileImageUrl());
		return preferenceResponse;
	}
}
