package linkfit.dto;

import java.time.LocalDateTime;
import linkfit.entity.UserBodyInfo;

public class UserBodyInfoResponse {

    private int height;
    private int weight;
    private String inbodyImageUrl;
    private LocalDateTime createDate;

    public UserBodyInfoResponse() {}

    public UserBodyInfoResponse(int height, int weight, String inbodyImageUrl, LocalDateTime createDate) {
        this.height = height;
        this.weight = weight;
        this.inbodyImageUrl = inbodyImageUrl;
        this.createDate = createDate;
    }

    public UserBodyInfoResponse(UserBodyInfo userBodyInfo) {
        this.height = userBodyInfo.getHeight();
        this.weight = userBodyInfo.getWeight();
        this.inbodyImageUrl = userBodyInfo.getInbodyImageUrl();
        this.createDate = userBodyInfo.getCreateDate();
    }
}
