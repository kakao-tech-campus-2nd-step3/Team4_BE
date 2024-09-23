package linkfit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TRAINER_TB", indexes = @Index(name = "idx_trainer_email", columnList = "email"))
public class Trainer extends Person {

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

    protected Trainer() {
        super();
    }

    public Trainer(String email, String password, String name, String gender) {
        super(email, password, name);
        this.gender = gender;
    }
}
