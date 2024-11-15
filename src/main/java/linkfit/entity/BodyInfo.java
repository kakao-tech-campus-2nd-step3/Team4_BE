package linkfit.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import linkfit.dto.BodyInfoResponse;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "BODY_INFO_TB")
@SQLDelete(sql = "UPDATE BODY_INFO_TB SET deleted = true WHERE id=?")
@SQLRestriction("deleted = false")
public class BodyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String inbodyImageUrl;

    private LocalDateTime createDate;

    private final boolean deleted = Boolean.FALSE;

    protected BodyInfo() {
    }

    public BodyInfo(User user, String inbodyImageUrl) {
        this.user = user;
        this.inbodyImageUrl = inbodyImageUrl;
        this.createDate = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public String getInbodyImageUrl() {
        return inbodyImageUrl;
    }

    public BodyInfoResponse toDto() {
        return new BodyInfoResponse(id, inbodyImageUrl, createDate);
    }
}
