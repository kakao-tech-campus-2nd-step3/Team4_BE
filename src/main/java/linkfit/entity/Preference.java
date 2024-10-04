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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Sports sports;

    @Column(nullable = false)
    private TrainerGender gender;

    @Column(nullable = false)
    private int range;

    @Column(nullable = false)
    private String goal;

    public Preference(User user, Sports sports, TrainerGender gender, int range, String goal) {
        this.user = user;
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

    public PreferenceResponse toDto(BodyInfo bodyInfo) {
        return new PreferenceResponse(user.getId(), user.getName(),
            bodyInfo.getInbodyImageUrl(), goal, user.getProfileImageUrl());
    }
}
