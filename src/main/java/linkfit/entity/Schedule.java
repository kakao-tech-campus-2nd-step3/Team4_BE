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
    @Column(name = "pt_id")
    private Pt pt;

    @Column(nullable = false)
    private LocalDateTime date;

    private boolean completed = Boolean.FALSE;

    protected Schedule() {
    }

    public Schedule(Pt pt, LocalDateTime date) {
        this.pt = pt;
        this.date = date;
    }

    public Pt getPt() {
        return pt;
    }

    public LocalDateTime getDate() {
        return date;
    };

    public boolean isCompleted() {
        return completed;
    }

    public void complete() {
        this.completed = Boolean.TRUE;
    }

    public ScheduleResponse toDto(){
        return new ScheduleResponse(id, completed,date);
    }
}
