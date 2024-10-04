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

@Entity
@Table(name = "PREFERENCE_TB")
public class Preference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Sports sports;

    @OneToOne
    @JoinColumn(nullable = false)
    private BodyInfo bodyInfo;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private int range;

    @Column(nullable = false)
    private String goal;

    public BodyInfo getBodyInfo() {
        return bodyInfo;
    }

    public int getRange() {
        return range;
    }

    protected Preference() {
    }

    public Preference(String gender, Sports sports, BodyInfo bodyInfo, int range, String goal) {
        this.gender = gender;
        this.sports = sports;
        this.bodyInfo = bodyInfo;
        this.range = range;
        this.goal = goal;
    }


    public PreferenceResponse toDto() {
        PreferenceResponse preferenceResponse = new PreferenceResponse(
            bodyInfo.getUser().getId(), bodyInfo.getUser().getName(),
            bodyInfo.getInbodyImageUrl(), goal, bodyInfo.getUser().getProfileImageUrl());
        return preferenceResponse;
    }
}
