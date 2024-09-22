package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Trainers")
public class Trainer extends PersonEntity{
	
	@ManyToOne
    @JoinColumn(name = "GYM_ID")
    private Gym gym;
	
	@Column(nullable = false)
	private String gender;
	
	public String getGender() {
		return gender;
	}
	
	public String getGymName() {
		return gym.getGymName();
	}
	
	public Trainer() {
		super();
	}
	
	public Trainer(String email, String password, String name, String gender) {
		super(email, password, name);
		this.gender = gender;
	}
}