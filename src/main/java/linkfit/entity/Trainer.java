package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Trainers")
public class Trainer extends PersonEntity{
	
	@Column(nullable = false)
	private String gender;
	
	public String getGender() {
		return gender;
	}
	
	public Trainer() {
		super();
	}
	
	public Trainer(String email, String password, String name, String gender) {
		super(email, password, name);
		this.gender = gender;
	}
}
