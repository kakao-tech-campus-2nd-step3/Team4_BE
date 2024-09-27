package linkfit.dto;

import java.util.List;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;

public class UserPtResponse {

    private Long trainerId;
    private String trainerName;
    private String gymName;
    private int count;
    private List<Schedule> schedules;

    public UserPtResponse(Pt pt, List<Schedule> schedules) {
        this.trainerId = pt.getTrainer().getId();
        this.trainerName = pt.getTrainer().getName();
        this.gymName = pt.getTrainer().getName();
        this.count = pt.getTotalCount();
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
