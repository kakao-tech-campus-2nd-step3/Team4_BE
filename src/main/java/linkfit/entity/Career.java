package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import linkfit.dto.CareerResponse;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "CAREER_TB")
@SQLDelete(sql = "UPDATE CAREER_TB SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Trainer trainer;

    @Column(nullable = false)
    private String career;

    private final boolean deleted = Boolean.FALSE;

    protected Career() {
    }

    public Career(Trainer trainer, String career) {
        this.trainer = trainer;
        this.career = career;
    }

    public Long getId() {
        return id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public String getCareer() {
        return career;
    }

    public CareerResponse toDto() {
        return new CareerResponse(getId(), getCareer());
    }
}