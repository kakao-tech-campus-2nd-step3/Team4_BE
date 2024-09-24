package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_INFORMATION;

import java.util.List;
import linkfit.dto.SuggestionRequest;
import linkfit.dto.TrainerPtResponse;
import linkfit.dto.UserPtResponse;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.InvalidIdException;
import linkfit.exception.NotFoundException;
import linkfit.repository.PtRepository;
import linkfit.repository.ScheduleRepository;
import linkfit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PtService {

    private final PtRepository ptRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Autowired
    public PtService(
        PtRepository ptRepository,
        ScheduleRepository scheduleRepository,
        UserRepository userRepository) {
        this.ptRepository = ptRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    public List<TrainerPtResponse> getAllPt(
        String authorization,
        Pageable pageable) {
        // 토큰 파싱하여 트레이너 정보 받아오기
        Trainer trainer = new Trainer();
        return ptRepository.findAllByTrainer(trainer, pageable)
            .stream()
            .map(Pt::getUser)
            .map(TrainerPtResponse::new)
            .toList();
    }

    public UserPtResponse getMyPt(
        String authorization) {
        // 토큰 파싱하여 유저 정보 받아오기
        User user = new User();
        Pt pt = ptRepository.findByUser(user)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_INFORMATION));
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new UserPtResponse(pt, schedules);
    }

    public void suggestPt(String authorization, SuggestionRequest suggestionRequest) {
        // 토큰 파싱하여 트레이너 정보 받아오기
        Trainer trainer = new Trainer();
        User user = userRepository.findById(suggestionRequest.userId())
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        Pt suggestion = new Pt(
            user,
            trainer,
            suggestionRequest.totalCount(),
            suggestionRequest.price()
        );
        ptRepository.save(suggestion);
    }
}
