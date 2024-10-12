package linkfit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "GYM_ADMIN_RELATION_TB")
public class GymAdminRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Gym gym;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Trainer trainer;

    protected GymAdminRelation() {
    }

    public GymAdminRelation(Gym gym, Trainer trainer) {
        this.gym = gym;
        this.trainer = trainer;
    }

    public Trainer getTrainer() {
        return trainer;
    }
}
