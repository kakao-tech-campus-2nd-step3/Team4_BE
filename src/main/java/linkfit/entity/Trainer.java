package linkfit.entity;

import static linkfit.exception.GlobalExceptionHandler.NOT_MATCH_PASSWORD;

import jakarta.persistence.*;
import linkfit.dto.TrainerProfileResponse;
import linkfit.exception.PasswordMismatchException;

@Entity
@Table(name = "TRAINER_TB", indexes = @Index(name = "IDX_TRAINER_EMAIL", columnList = "EMAIL"))
public class Trainer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String profileImageUrl;

	@ManyToOne
	@JoinColumn(nullable = true)
	private Gym gym;

	@Column(nullable = false)
	private String gender;

	public Long getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getName() {
		return name;
	}

	public String getGender() {
		return gender;
	}
	
	public String getProfileImageUrl() {
    	return profileImageUrl;
    }

	public Gym getGym() {
		return gym;
	}
	
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	protected Trainer() {
	}

	public Trainer(String email, String password, String name, String gender) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.gender = gender;
	}
	
	public void validatePassword(String inputPassword) {
        if (!inputPassword.equals(this.password)) {
            throw new PasswordMismatchException(NOT_MATCH_PASSWORD);
        }
    }

	public TrainerProfileResponse toDto() {
		return new TrainerProfileResponse(getName(), getGender(), getProfileImageUrl(), getGym().getName());
	}
}
