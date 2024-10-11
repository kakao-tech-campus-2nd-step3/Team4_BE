package linkfit.dto;

import java.util.List;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;

public record UserPtResponse(Long trainerId, String trainerName, String gymName, int count,
                             List<Schedule> schedules) {

    public UserPtResponse(Pt pt, List<Schedule> schedules) {
        this(pt.getTrainer().getId(), pt.getTrainer().getName(), pt.getTrainer().getGym().getName(),
            pt.getTotalCount(), schedules);
    }
}