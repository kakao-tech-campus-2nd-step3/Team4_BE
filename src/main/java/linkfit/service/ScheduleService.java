package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.ALREADY_COMPLETED_SCHEDULE;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_PT;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_SCHEDULE;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_TRAINER;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_USER;
import static linkfit.exception.GlobalExceptionHandler.NOT_OWNER;
import static linkfit.exception.GlobalExceptionHandler.UNRELATED_SCHEDULE;

import com.amazonaws.services.kms.model.NotFoundException;
import java.util.List;
import linkfit.dto.ScheduleRequest;
import linkfit.dto.ScheduleResponse;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.PermissionException;
import linkfit.repository.PtRepository;
import linkfit.repository.ScheduleRepository;
import linkfit.repository.TrainerRepository;
import linkfit.repository.UserRepository;
import linkfit.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PtRepository ptRepository;
    private final TrainerRepository trainerRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public ScheduleService(
        ScheduleRepository scheduleRepository,
        PtRepository ptRepository,
        TrainerRepository trainerRepository,
        JwtUtil jwtUtil, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.ptRepository = ptRepository;
        this.trainerRepository = trainerRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public ScheduleResponse getSchedules(Long ptId) {
        Pt pt = findPt(ptId);
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new ScheduleResponse(pt, schedules);
    }

    public void registerSchedule(
        String authorization,
        Long ptId,
        ScheduleRequest scheduleRequest) {
        Pt pt = findPt(ptId);
        Trainer trainer = getTrainer(authorization);
        if (!pt.getTrainer().equals(trainer)) {
            throw new PermissionException(NOT_OWNER);
        }
        Schedule schedule = scheduleRequest.toEntity(pt);
        scheduleRepository.save(schedule);
    }

    private Pt findPt(Long ptId) {
        return ptRepository.findById(ptId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_PT));
    }

    public void completeSchedule(String authorization, Long ptId, Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);
        isRelatedSchedule(schedule, ptId);
        isUserOwner(authorization, schedule);
        schedule.complete();
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(String authorization, Long ptId, Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);
        isRelatedSchedule(schedule, ptId);
        isTrainerOwner(authorization, schedule);
        if (schedule.getStatus() == 1) {
            throw new PermissionException(ALREADY_COMPLETED_SCHEDULE);
        }
        scheduleRepository.delete(schedule);
    }

    private Schedule findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_SCHEDULE));
    }

    private void isRelatedSchedule(Schedule schedule, Long ptId) {
        if (!schedule.getPt().getId().equals(ptId)) {
            throw new PermissionException(UNRELATED_SCHEDULE);
        }
    }

    private Trainer getTrainer(String authorization) {
        Long id = jwtUtil.parseToken(authorization);
        return trainerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_TRAINER));
    }

    private User getUser(String authorization) {
        Long id = jwtUtil.parseToken(authorization);
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
    }

    private void isTrainerOwner(String authorization, Schedule schedule) {
        Trainer trainer = getTrainer(authorization);
        if (!trainer.equals(schedule.getPt().getTrainer())) {
            throw new PermissionException(NOT_OWNER);
        }
    }

    private void isUserOwner(String authorization, Schedule schedule) {
        User user = getUser(authorization);
        if (!user.equals(schedule.getPt().getUser())) {
            throw new PermissionException(NOT_OWNER);
        }
    }
}
