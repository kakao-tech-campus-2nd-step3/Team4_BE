package linkfit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import linkfit.dto.UserBodyInfoRequest;

@Entity
public class UserBodyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    private int height;

    private int weight;

    private String inbodyImageUrl;

    private LocalDateTime createDate;

    public UserBodyInfo() {}

    public UserBodyInfo(User user, UserBodyInfoRequest userBodyInfoRequest) {
        this.user = user;
        this.height = userBodyInfoRequest.getHeight();
        this.weight = userBodyInfoRequest.getWeight();
        this.inbodyImageUrl = userBodyInfoRequest.getInbodyImage().toString();
        this.createDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getInbodyImageUrl() {
        return inbodyImageUrl;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }
}
