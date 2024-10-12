package linkfit.service;

import java.util.List;
import linkfit.dto.ScheduleRequest;
import linkfit.dto.ScheduleResponse;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.PtRepository;
import linkfit.repository.ScheduleRepository;
import linkfit.repository.TrainerRepository;
import linkfit.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PtRepository ptRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, PtRepository ptRepository,
        TrainerRepository trainerRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.ptRepository = ptRepository;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
    }

    public ScheduleResponse getSchedules(Long ptId) {
        Pt pt = findPt(ptId);
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new ScheduleResponse(pt, schedules);
    }

    public void registerSchedule(
        Long trainerId,
        Long ptId,
        ScheduleRequest scheduleRequest) {
        Pt pt = findPt(ptId);
        Trainer trainer = getTrainer(trainerId);
        if (!pt.getTrainer().equals(trainer)) {
            throw new PermissionException("not.owner");
        }
        Schedule schedule = scheduleRequest.toEntity(pt);
        scheduleRepository.save(schedule);
    }

    private Pt findPt(Long ptId) {
        return ptRepository.findById(ptId)
            .orElseThrow(() -> new NotFoundException("not.found.pt"));
    }

    public void completeSchedule(Long userId, Long ptId, Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);
        isRelatedSchedule(schedule, ptId);
        isUserOwner(userId, schedule);
        schedule.complete();
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long trainerId, Long ptId, Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);
        isRelatedSchedule(schedule, ptId);
        isTrainerOwner(trainerId, schedule);
        if (schedule.getIsCompleted()) {
            throw new PermissionException("already.completed.schedule");
        }
        scheduleRepository.delete(schedule);
    }

    private Schedule findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new NotFoundException("not.found.schedule"));
    }

    private void isRelatedSchedule(Schedule schedule, Long ptId) {
        if (!schedule.getPt().getId().equals(ptId)) {
            throw new PermissionException("unrelated.schedule");
        }
    }

    private Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    private void isTrainerOwner(Long trainerId, Schedule schedule) {
        Trainer trainer = getTrainer(trainerId);
        if (!trainer.equals(schedule.getPt().getTrainer())) {
            throw new PermissionException("not.owner");
        }
    }

    private void isUserOwner(Long userId, Schedule schedule) {
        User user = getUser(userId);
        if (!user.equals(schedule.getPt().getUser())) {
            throw new PermissionException("not.owner");
        }
    }
}
