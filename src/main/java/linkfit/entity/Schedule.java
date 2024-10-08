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
    private LocalDateTime startTime;

    private int status = 0;

    protected Schedule() {
    }

    public Schedule(Pt pt, LocalDateTime startTime) {
        this.pt = pt;
        this.startTime = startTime;
        this.status = 0;
    }

    public Schedule(Pt pt, LocalDateTime startTime, String content) {
        this.pt = pt;
        this.startTime = startTime;
        this.content = content;
        this.status = 0;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getStatus() {
        return status;
    }

    public void complete() {
        this.status = 1;
    }
}
