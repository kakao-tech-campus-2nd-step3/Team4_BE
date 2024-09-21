package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Entity
public class OnGoingPt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Trainer trainer;

    @PositiveOrZero
    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private LocalDate startDate;

    public OnGoingPt() {}

    public OnGoingPt(User user, Trainer trainer, int count, LocalDate startDate) {
        this.user = user;
        this.trainer = trainer;
        this.count = count;
        this.startDate = startDate;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public int getCount() {
        return count;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
