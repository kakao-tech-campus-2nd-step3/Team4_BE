package linkfit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;

public record ScheduleRequest(@NotNull
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                              LocalDateTime startTime, String content) {

    public Schedule toEntity(Pt pt) {
        if (content == null) {
            return new Schedule(pt, startTime);
        }
        return new Schedule(pt, startTime, content);
    }
}