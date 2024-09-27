package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
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

    // 0: 예정된 스케쥴, 1: 완료된 스케쥴
    @Column(nullable = false)
    private int status = 0;

    protected Schedule() {}

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

    public void complete() {
        this.status = 1;
    }
}
