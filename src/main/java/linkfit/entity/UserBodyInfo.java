package linkfit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import linkfit.dto.UserBodyInfoResponse;

@Entity
@Table(name = "UserBodyInfo")
public class UserBodyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private String inbodyImageUrl;

    private LocalDateTime createDate;

    protected UserBodyInfo() {
    }

    public UserBodyInfo(User user, String inbodyImageUrl) {
        this.user = user;
        this.inbodyImageUrl = inbodyImageUrl;
        this.createDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getInbodyImageUrl() {
        return inbodyImageUrl;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public UserBodyInfoResponse toDto() {
        return new UserBodyInfoResponse(
            getInbodyImageUrl(),
            getCreateDate()
        );
    }
}
