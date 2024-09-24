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

@Entity
@Table(name = "Preferences")
public class Preference {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "sprots_type_id", nullable = false)
	private SprotsType sprotsType;
	
	@OneToOne
	@JoinColumn(name = "user_body_info_id", nullable = false)
	private UserBodyInfo userBodyInfo;
	
	@Column(nullable = false)
	private String gender;
	
	@Column(nullable = false)
	private int range;
	
	@Column(nullable = false)
	private String goal;
	
	public void setUserBodyInfo(UserBodyInfo userBodyInfo) {
		this.userBodyInfo = userBodyInfo;
	}
	
	public Preference() {}
	
	public Preference(String gender, SprotsType sprotsType, int range, String goal) {
		this.gender = gender;
		this.sprotsType = sprotsType;
		this.range = range;
		this.goal = goal;
	}
}
