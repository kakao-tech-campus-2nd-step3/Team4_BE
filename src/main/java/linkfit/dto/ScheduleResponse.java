package linkfit.dto;

import java.time.LocalDateTime;

public record ScheduleResponse(Long scheduleId, boolean isCompleted, LocalDateTime date) {

}