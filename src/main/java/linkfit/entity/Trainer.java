package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import linkfit.dto.TrainerProfileResponse;
import linkfit.exception.PasswordMismatchException;
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

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'https://nurspace-bucket.s3.ap-northeast-2.amazonaws.com/default_profile.jpg'")
    private String profileImageUrl;

    @ManyToOne
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
