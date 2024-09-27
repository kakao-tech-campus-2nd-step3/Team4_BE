package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.ALREADY_COMPLETED_SCHEDULE;
import static linkfit.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static linkfit.exception.GlobalExceptionHandler.UNRELATED_SCHEDULE;

import com.amazonaws.services.kms.model.NotFoundException;
import java.util.List;
import linkfit.dto.ScheduleRequest;
import linkfit.dto.ScheduleResponse;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;
import linkfit.exception.InvalidIdException;
import linkfit.exception.PermissionException;
import linkfit.repository.PtRepository;
import linkfit.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final PtRepository ptRepository;

    @Autowired
    public ScheduleService(
        ScheduleRepository scheduleRepository,
        PtRepository ptRepository) {
        this.scheduleRepository = scheduleRepository;
        this.ptRepository = ptRepository;
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
        // 토큰 파싱하여 트레이너 정보 반환
        //Trainer trainer = new Trainer();
        //if(!pt.getTrainer().equals(trainer))
            //throw new PermissionException(NO_PERMISSION);
        Schedule schedule = scheduleRequest.toEntity(pt);
        scheduleRepository.save(schedule);
    }

    private Pt findPt(Long ptId) {
        return ptRepository.findById(ptId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
    }

    public void completeSchedule(String authorization, Long ptId, Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);
        isRelatedSchedule(schedule, ptId);
        schedule.complete();
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(String authorization, Long ptId, Long scheduleId) {
        Schedule schedule = findSchedule(scheduleId);
        isRelatedSchedule(schedule, ptId);
        if(schedule.getStatus() == 1)
            throw new PermissionException(ALREADY_COMPLETED_SCHEDULE);
        scheduleRepository.delete(schedule);
    }

    private Schedule findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(() -> new NotFoundException(NOT_EXIST_ID));
    }

    private void isRelatedSchedule(Schedule schedule, Long ptId) {
        if(!schedule.getPt().getId().equals(ptId))
            throw new PermissionException(UNRELATED_SCHEDULE);
    }
}
