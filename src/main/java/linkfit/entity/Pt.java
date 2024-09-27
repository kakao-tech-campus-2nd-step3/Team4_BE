package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import linkfit.dto.PtSuggestionResponse;

@Entity
@Table(name = "PT_TB")
public class Pt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Trainer trainer;

    @Positive
    @Column(nullable = false)
    private int totalCount;

    @PositiveOrZero
    @Column(nullable = false)
    private int price;

    private LocalDateTime startDate;

    // 0: 대기중, 1: 회수된 제안, 2: 거절된 제안, 3: 수락된 제안
    @Column(nullable = false)
    private int status = 0;

    protected Pt() {}

    public Pt(User user, Trainer trainer, int totalCount, int price) {
        this.user = user;
        this.trainer = trainer;
        this.totalCount = totalCount;
        this.price = price;
    }

    public Pt(Long id, User user, Trainer trainer, int totalCount, int price, int status) {
        this.id = id;
        this.user = user;
        this.trainer = trainer;
        this.totalCount = totalCount;
        this.price = price;
        this.status = status;
    }

    public Pt(Long id, User user, Trainer trainer, int totalCount, int price, int status, LocalDateTime startDate) {
        this.id = id;
        this.user = user;
        this.trainer = trainer;
        this.totalCount = totalCount;
        this.price = price;
        this.status = status;
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

    public int getTotalCount() {
        return totalCount;
    }

    public int getPrice() {
        return price;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public int getStatus() {
        return status;
    }

    public PtSuggestionResponse toDto() {
        return new PtSuggestionResponse(id, user, totalCount, price, status);
    }

    public void reject() {
        this.status = 1;
    }

    public void accept() {
        this.status = 3;
        this.startDate = LocalDateTime.now();
    }
}
