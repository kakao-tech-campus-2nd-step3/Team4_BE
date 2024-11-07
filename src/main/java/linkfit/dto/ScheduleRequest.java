package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;

public record ScheduleRequest(@NotNull LocalDateTime startTime) {

    public Schedule toEntity(Pt pt) {
        return new Schedule(pt, startTime);
    }
}