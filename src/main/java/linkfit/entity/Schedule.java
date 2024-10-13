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

@Entity
@Table(name = "SCHEDULE_TB")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Pt pt;

    private String content;

    @Column(nullable = false)
    private LocalDateTime atTime;

    private boolean isCompleted = Boolean.FALSE;

    protected Schedule() {
    }

    public Schedule(Pt pt, LocalDateTime atTime) {
        this.pt = pt;
        this.atTime = atTime;
    }

    public Schedule(Pt pt, LocalDateTime atTime, String content) {
        this.pt = pt;
        this.atTime = atTime;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public Pt getPt() {
        return pt;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getAtTime() {
        return atTime;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void complete() {
        this.isCompleted = Boolean.TRUE;
    }
}
