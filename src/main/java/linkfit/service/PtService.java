package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_INFORMATION;

import java.util.List;
import linkfit.dto.TrainerPtResponse;
import linkfit.dto.UserPtResponse;
import linkfit.entity.OnGoingPt;
import linkfit.entity.Schedule;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.repository.OnGoingPtRepository;
import linkfit.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PtService {

    private final OnGoingPtRepository onGoingPtRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public PtService(
        OnGoingPtRepository onGoingPtRepository,
        ScheduleRepository scheduleRepository
    ) {
        this.onGoingPtRepository = onGoingPtRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<TrainerPtResponse> getAllPt(
        String authorization,
        Pageable pageable) {
        // 토큰 파싱하여 트레이너 정보 받아오기
        Trainer trainer = new Trainer();
        return onGoingPtRepository.findAllByTrainer(trainer, pageable)
            .stream()
            .map(OnGoingPt::getUser)
            .map(TrainerPtResponse::new)
            .toList();
    }

    public UserPtResponse getMyPt(
        String authorization) {
        // 토큰 파싱하여 유저 정보 받아오기
        User user = new User();
        OnGoingPt onGoingPt = onGoingPtRepository.findByUser(user)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_INFORMATION));
        List<Schedule> schedules = scheduleRepository.findAllByOnGoingPt(onGoingPt);
        return new UserPtResponse(onGoingPt, schedules);
    }
}
