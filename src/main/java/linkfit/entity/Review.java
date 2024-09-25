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

import linkfit.dto.ReviewResponse;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "REVIEW_TB")
@SQLDelete(sql = "UPDATE review_tb SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "TRAINER_ID", nullable = false)
    private Trainer trainer;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int score;

    @CreatedDate
    private LocalDateTime createdDate;

    private boolean deleted = Boolean.FALSE;

    public Review() {
    }

    public Review(User user, Trainer trainer, String content, int score) {
        this.user = user;
        this.trainer = trainer;
        this.content = content;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public Long getUsersId() {
        return user.getId();
    }

    public Long getTrainersId() {
        return trainer.getId();
    }

    public String getContent() {
        return content;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

}
