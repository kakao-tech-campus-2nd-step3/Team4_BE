package linkfit.dto;

import java.util.List;
import linkfit.entity.OnGoingPt;
import linkfit.entity.Schedule;

public class UserPtResponse {

    private Long trainerId;
    private String trainerName;
    private String gymName;
    private int count;
    private List<Schedule> schedules;

    public UserPtResponse(OnGoingPt onGoingPt, List<Schedule> schedules) {
        this.trainerId = onGoingPt.getTrainer().getId();
        this.trainerName = onGoingPt.getTrainer().getName();
        this.gymName = onGoingPt.getTrainer().getGymName();
        this.count = onGoingPt.getCount();
        this.schedules = schedules;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public String getGymName() {
        return gymName;
    }

    public int getCount() {
        return count;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }
}
