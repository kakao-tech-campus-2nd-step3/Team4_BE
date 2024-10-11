package linkfit.entity;

import jakarta.persistence.*;

import linkfit.dto.TrainerProfileResponse;
import linkfit.exception.PasswordMismatchException;
import linkfit.status.TrainerGender;
import org.springframework.beans.factory.annotation.Value;

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
    private Gym gym;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private TrainerGender gender;

    @Value("${defaultImageUrl}")
    private String defaultProfileImageUrl;

    protected Trainer() {
    }

    public Trainer(String email, String password, String name, TrainerGender gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.gender = gender;
    }

    @PrePersist
    public void prePersist() {
        if (this.profileImageUrl == null || this.profileImageUrl.isEmpty()) {
            this.profileImageUrl = defaultProfileImageUrl;
        }
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

    public Gym getGym() {
        return gym;
    }

    public void validatePassword(String inputPassword) {
        if (!inputPassword.equals(this.password)) {
            throw new PasswordMismatchException("not.match.password");
        }
    }

    public TrainerProfileResponse toDto() {
        if(gym == null) {
            return new TrainerProfileResponse(name, gender, profileImageUrl, null);
        }
        return new TrainerProfileResponse(name, gender, profileImageUrl, gym.getName());
    }
}
