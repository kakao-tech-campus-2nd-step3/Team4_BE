package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "SCHEDULE_TB")
public class Schedule {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Pt pt;

    @Column(nullable = false)
    private LocalDateTime atDate;

    private boolean status;

    protected Schedule() {}

    public Schedule(Pt pt, LocalDateTime atDate, boolean status) {
        this.pt = pt;
        this.atDate = atDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Pt getPt() {
        return pt;
    }

    public LocalDateTime getAtDate() {
        return atDate;
    }

    public boolean isStatus() {
        return status;
    }
}
