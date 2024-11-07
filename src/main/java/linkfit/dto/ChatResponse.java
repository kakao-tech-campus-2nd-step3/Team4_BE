package linkfit.dto;

import java.util.List;
import linkfit.entity.Message;

public record ChatResponse(Long roomId, List<MessageResponse> messages) {

}
