package linkfit.dto;

import java.time.LocalDateTime;
import linkfit.entity.UserBodyInfo;

public class UserBodyInfoResponse {

    private String inbodyImageUrl;
    private LocalDateTime createDate;

    public UserBodyInfoResponse() {}

    public UserBodyInfoResponse(String inbodyImageUrl, LocalDateTime createDate) {

        this.inbodyImageUrl = inbodyImageUrl;
        this.createDate = createDate;
    }

    public UserBodyInfoResponse(UserBodyInfo userBodyInfo) {
        this.inbodyImageUrl = userBodyInfo.getInbodyImageUrl();
        this.createDate = userBodyInfo.getCreateDate();
    }
}
