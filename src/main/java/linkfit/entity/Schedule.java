package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import linkfit.dto.ScheduleResponse;

@Entity
@Table(name = "SCHEDULE_TB")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Pt pt;

    @Column(nullable = false)
    private LocalDateTime atTime;

    private boolean isCompleted = Boolean.FALSE;

    protected Schedule() {
    }

    public Schedule(Pt pt, LocalDateTime atTime) {
        this.pt = pt;
        this.atTime = atTime;
    }

    public Pt getPt() {
        return pt;
    }

    public LocalDateTime getAtTime() {
        return atTime;
    };

    public boolean getCompleted() {
        return isCompleted;
    }

    public void complete() {
        this.isCompleted = Boolean.TRUE;
    }

    public ScheduleResponse toDto(){
        return new ScheduleResponse(isCompleted,atTime);
    }
}
