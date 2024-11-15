package linkfit.entity;

import jakarta.persistence.*;
import linkfit.component.DefaultImageProvider;
import linkfit.dto.TrainerProfileResponse;
import linkfit.status.TrainerGender;

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

    @Column(nullable = true)
    private String profileImageUrl;

    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TrainerGender gender;

    protected Trainer() {
    }

    public Trainer(String email, String password, String name, TrainerGender gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public TrainerGender getGender() {
        return gender;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    public Gym getGym() {
        return gym;
    }

    public String getPassword() {
        return password;
    }

    public TrainerProfileResponse toDto() {
        if (gym == null) {
            return new TrainerProfileResponse(name, gender, profileImageUrl, null);
        }
        return new TrainerProfileResponse(name, gender, profileImageUrl, gym.getName());
    }
}
