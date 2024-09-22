package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Schedule {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private OnGoingPt onGoingPt;

    @Column(nullable = false)
    private LocalDateTime atDate;

    private boolean status;

    public Schedule() {}

    public Schedule(OnGoingPt onGoingPt, LocalDateTime atDate, boolean status) {
        this.onGoingPt = onGoingPt;
        this.atDate = atDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public OnGoingPt getOnGoingPt() {
        return onGoingPt;
    }

    public LocalDateTime getAtDate() {
        return atDate;
    }

    public boolean isStatus() {
        return status;
    }
}
