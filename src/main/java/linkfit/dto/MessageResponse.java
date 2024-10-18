package linkfit.dto;

import java.time.LocalDateTime;
import linkfit.status.Role;

public record MessageResponse(Long id, String content, Role sender, LocalDateTime date) {

}
