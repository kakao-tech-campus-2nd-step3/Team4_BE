package linkfit.dto;

import java.util.List;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;

public record ScheduleResponse(int totalCount, List<Schedule> schedules) {
    public ScheduleResponse(Pt pt, List<Schedule> schedules) {
        this(pt.getTotalCount(), schedules);
    }
}
