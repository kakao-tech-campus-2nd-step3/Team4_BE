package linkfit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
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

    private LocalDate createDate;

    public UserBodyInfo() {}

    public UserBodyInfo(User user, UserBodyInfoRequest userBodyInfoRequest) {
        this.user = user;
        this.height = userBodyInfoRequest.getHeight();
        this.weight = userBodyInfoRequest.getWeight();
        this.inbodyImageUrl = userBodyInfoRequest.getInbodyImage().toString();
        this.createDate = LocalDate.now();
    }
}
