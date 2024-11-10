package linkfit.dto;

import java.time.LocalDateTime;
import java.util.List;
import linkfit.entity.Schedule;

public record ScheduleResponse(Long id, boolean isCompleted, LocalDateTime date) {

}