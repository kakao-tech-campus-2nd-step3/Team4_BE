package linkfit.dto;

import java.time.LocalDateTime;
import java.util.List;
import linkfit.entity.Schedule;

public record ScheduleResponse(boolean isCompleted, LocalDateTime date) {

}