package linkfit.dto;

import java.util.List;
import linkfit.entity.Schedule;

public record ScheduleResponse(int totalCount, List<Schedule> schedules) {

}