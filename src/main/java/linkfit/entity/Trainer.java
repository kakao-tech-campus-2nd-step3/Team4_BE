package linkfit.entity;

import jakarta.persistence.*;
import linkfit.dto.TrainerProfileResponse;

@Entity
@Table(name = "TRAINER_TB", indexes = @Index(name = "IDX_TRAINER_EMAIL", columnList = "EMAIL"))
public class Trainer extends Person<TrainerProfileResponse> {

    @ManyToOne
    @JoinColumn(nullable = true)
    private Gym gym;

    @Column(nullable = false)
    private String gender;

    public String getGender() {
        return gender;
    }
    
    public Gym getGym() {
		return gym;
	}

    protected Trainer() {
        super();
    }

    public Trainer(String email, String password, String name, String gender) {
        super(email, password, name);
        this.gender = gender;
    }

    @Override
    public TrainerProfileResponse toDto() {
        return new TrainerProfileResponse(this.getEmail(), this.getPassword(), this.getName(), this.getGender());
    }
}
