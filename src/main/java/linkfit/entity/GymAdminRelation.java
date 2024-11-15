package linkfit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GYM_ADMIN_RELATION_TB")
public class GymAdminRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gym_id", nullable = false)
    private Gym gym;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
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
