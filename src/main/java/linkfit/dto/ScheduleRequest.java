package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;

public record ScheduleRequest(@NotNull LocalDateTime startTime, String content) {
    public Schedule toEntity(Pt pt) {
        if(content == null) {
            return new Schedule(pt, startTime);
        }
        return new Schedule(pt, startTime, content);
    }
}
