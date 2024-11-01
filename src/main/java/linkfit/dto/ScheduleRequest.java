package linkfit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;

public record ScheduleRequest(@NotNull
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                              LocalDateTime startTime) {

    public Schedule toEntity(Pt pt) {
        return new Schedule(pt, startTime);
    }
}