package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_EXIST_ID;

import java.util.List;
import linkfit.dto.ScheduleResponse;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;
import linkfit.exception.InvalidIdException;
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
        Pt pt = ptRepository.findById(ptId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new ScheduleResponse(pt, schedules);
    }
}
