package linkfit.service;

import java.util.List;
import java.util.Objects;
import linkfit.dto.ScheduleRequest;
import linkfit.dto.ScheduleResponse;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.PtRepository;
import linkfit.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PtRepository ptRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, PtRepository ptRepository) {
        this.scheduleRepository = scheduleRepository;
        this.ptRepository = ptRepository;
    }

    public ScheduleResponse getSchedules(Long ptId) {
        Pt pt = getPtById(ptId);
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new ScheduleResponse(pt.getTotalCount(), schedules);
    }

    public void registerSchedule(Long trainerId, Long ptId, ScheduleRequest scheduleRequest) {
        Pt pt = getPtById(ptId);
        validateTrainerPTOwnership(pt, trainerId);
        Schedule schedule = scheduleRequest.toEntity(pt);
        scheduleRepository.save(schedule);
    }

    public void completeSchedule(Long userId, Long ptId, Long scheduleId) {
        Schedule schedule = getScheduleById(scheduleId);
        validatePTSchedule(schedule, ptId);
        validateScheduleOwnership(schedule, userId);
        schedule.complete();
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long trainerId, Long ptId, Long scheduleId) {
        Schedule schedule = getScheduleById(scheduleId);
        validatePTSchedule(schedule, ptId);
        validateScheduleOwnership(schedule, trainerId);
        validateScheduleIsComplete(schedule);
        scheduleRepository.delete(schedule);
    }

    private Pt getPtById(Long ptId) {
        return ptRepository.findById(ptId)
            .orElseThrow(() -> new NotFoundException("not.found.pt"));
    }

    private Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new NotFoundException("not.found.schedule"));
    }

    private void validateScheduleIsComplete(Schedule schedule) {
        if (schedule.getIsCompleted()) {
            throw new PermissionException("already.completed.schedule");
        }
    }

    private void validatePTSchedule(Schedule schedule, Long ptId) {
        if (!schedule.getPt().getId().equals(ptId)) {
            throw new PermissionException("unrelated.schedule");
        }
    }

    private void validateTrainerPTOwnership(Pt pt, Long memberId) {
        Long ptTrainerId = pt.getTrainer().getId();
        if (!Objects.equals(ptTrainerId, memberId)) {
            throw new PermissionException("not.owner");
        }
    }

    private void validateScheduleOwnership(Schedule schedule, Long memberId) {
        Pt pt = schedule.getPt();
        Long scheduleTrainerId = pt.getTrainer().getId();
        Long scheduleUserId = pt.getUser().getId();
        if (!Objects.equals(memberId, scheduleTrainerId)
            && !Objects.equals(memberId, scheduleUserId)) {
            throw new PermissionException("not.owner");
        }
    }
}