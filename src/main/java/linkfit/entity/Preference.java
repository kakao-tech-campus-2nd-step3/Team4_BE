package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import linkfit.dto.PreferenceResponse;
import linkfit.status.TrainerGender;

@Entity
@Table(name = "PREFERENCE_TB")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(nullable = false)
    private BodyInfo bodyInfo;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Sports sports;

    @Enumerated(EnumType.STRING)
    private TrainerGender gender;

    @Column(nullable = false)
    private int range;

    @Column(nullable = false)
    private String goal;

    public Preference(User user, BodyInfo bodyInfo, Sports sports, TrainerGender gender, int range,
        String goal) {
        this.user = user;
        this.bodyInfo = bodyInfo;
        this.sports = sports;
        this.gender = gender;
        this.range = range;
        this.goal = goal;
    }

    protected Preference() {
    }

    public int getRange() {
        return range;
    }

    public User getUser() {
        return user;
    }

    public boolean isValidTrainerGender(TrainerGender gender) {
        if (this.gender == null) {
            return true;
        }
        return this.gender.equals(gender);
    }

    public PreferenceResponse toDto() {
        return new PreferenceResponse(user.getId(), user.getName(),
            bodyInfo.getInbodyImageUrl(), goal, user.getProfileImageUrl());
    }
}
