package linkfit.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

@Entity
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

    protected Career() {
    }

    public Career(Trainer trainer, String career) {
        this.trainer = trainer;
        this.career = career;
    }
}