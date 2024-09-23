package linkfit.dto;

import java.time.LocalDateTime;
import linkfit.entity.BodyInfo;

public class UserBodyInfoResponse {

    private String inbodyImageUrl;
    private LocalDateTime createDate;

    public UserBodyInfoResponse() {
    }

    public UserBodyInfoResponse(String inbodyImageUrl, LocalDateTime createDate) {

        this.inbodyImageUrl = inbodyImageUrl;
        this.createDate = createDate;
    }

    public UserBodyInfoResponse(BodyInfo bodyInfo) {
        this.inbodyImageUrl = bodyInfo.getInbodyImageUrl();
        this.createDate = bodyInfo.getCreateDate();
    }
}
