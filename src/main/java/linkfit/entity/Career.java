package linkfit.entity;

import jakarta.persistence.*;

@Entity
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TRAINER_ID", nullable = false)
    private Trainer trainer;

    @Column(nullable = false)
    private String career;

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