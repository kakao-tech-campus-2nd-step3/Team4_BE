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
import linkfit.dto.ReviewRequest;
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
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Trainer trainer;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int score;

    @CreatedDate
    private LocalDateTime createdDate;

    private final boolean deleted = Boolean.FALSE;

    protected Review() {
    }

    public Review(User user, Trainer trainer, ReviewRequest reviewRequest) {
        this.user = user;
        this.trainer = trainer;
        this.content = reviewRequest.content();
        this.score = reviewRequest.score();
        this.createdDate = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public ReviewResponse toDto() {
        return new ReviewResponse(id, content, createdDate, score);
    }
}
