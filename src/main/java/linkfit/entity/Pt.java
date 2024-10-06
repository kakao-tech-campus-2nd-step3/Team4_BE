package linkfit.entity;

import static linkfit.status.PtStatus.APPROVAL;
import static linkfit.status.PtStatus.RECALL;
import static linkfit.status.PtStatus.REFUSE;
import static linkfit.status.PtStatus.WAITING;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import linkfit.dto.ProgressPtListResponse;
import linkfit.dto.ReceivePtSuggestResponse;
import linkfit.dto.SendPtSuggestResponse;
import linkfit.status.PtStatus;

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

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PtStatus status = WAITING;

    protected Pt() {
    }

    public Pt(User user, Trainer trainer, int totalCount, int price) {
        this.user = user;
        this.trainer = trainer;
        this.totalCount = totalCount;
        this.price = price;
    }

    public Pt(Long id, User user, Trainer trainer, int totalCount, int price, PtStatus status) {
        this.id = id;
        this.user = user;
        this.trainer = trainer;
        this.totalCount = totalCount;
        this.price = price;
        this.status = status;
    }

    public Pt(Long id, User user, Trainer trainer, int totalCount, int price, PtStatus status,
        LocalDateTime startDate) {
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

    public PtStatus getStatus() {
        return status;
    }

    public SendPtSuggestResponse toSendDto() {
        return new SendPtSuggestResponse(id, user.getName(), user.getProfileImageUrl(), totalCount,
            price, status);
    }

    public ReceivePtSuggestResponse toReceiveDto() {
        return new ReceivePtSuggestResponse(id, trainer.getId(), trainer.getName(),
            trainer.getProfileImageUrl(), trainer.getGym().getName(), totalCount, price);
    }

    public ProgressPtListResponse toProgressDto() {
        return new ProgressPtListResponse(id, user.getId(), user.getName(), user.getProfileImageUrl());
    }

    public void refuse() {
        this.status = REFUSE;
    }

    public void approval() {
        this.status = APPROVAL;
        this.startDate = LocalDateTime.now();
    }

    public void recall() {
        this.status = RECALL;
    }
}
