package linkfit.entity;

import jakarta.persistence.*;
import linkfit.dto.CareerResponse;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "CAREER_TB")
@SQLDelete(sql = "UPDATE career SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TRAINER_ID", nullable = false)
    private Trainer trainer;

    @Column(nullable = false)
    private String career;

    private boolean deleted = Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public String getCareer() {
        return career;
    }

    protected Career() {}

    public Career(Trainer trainer, String career) {
        this.trainer = trainer;
        this.career = career;
    }

    public CareerResponse toDto() {
        return new CareerResponse(getId(), getTrainer().getId(), getCareer());
    }
}