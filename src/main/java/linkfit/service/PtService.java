package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_EXIST_ID;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_INFORMATION;
import static linkfit.exception.GlobalExceptionHandler.NO_PERMISSION;

import java.util.List;
import linkfit.dto.PtSuggestionRequest;
import linkfit.dto.PtSuggestionResponse;
import linkfit.dto.PtSuggestionUpdateRequest;
import linkfit.dto.TrainerPtResponse;
import linkfit.dto.UserPtResponse;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.InvalidIdException;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.PtRepository;
import linkfit.repository.ScheduleRepository;
import linkfit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.PathMatcher;

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

    public void suggestPt(String authorization, PtSuggestionRequest ptSuggestionRequest) {
        // 토큰 파싱하여 트레이너 정보 받아오기
        Trainer trainer = new Trainer();
        User user = userRepository.findById(ptSuggestionRequest.userId())
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
        Pt suggestion = new Pt(
            user,
            trainer,
            ptSuggestionRequest.totalCount(),
            ptSuggestionRequest.price()
        );
        ptRepository.save(suggestion);
    }

    public List<PtSuggestionResponse> getAllPtSuggestion(String authorization, Pageable pageable) {
        // 토큰 파싱하여 트레이너 정보 받아오기
        Trainer trainer = new Trainer();
        return ptRepository.findAllByTrainer(trainer, pageable).stream()
            .map(Pt::toDto)
            .toList();
    }

    public void recallPtSuggestion(String authorization, Long ptId) {
        // 토큰 파싱하여 트레이너 정보 받아오기
        Trainer trainer = new Trainer();
        Pt suggestion = findSuggestion(ptId);
        if(!suggestion.getTrainer().equals(trainer)) {
            throw new PermissionException(NO_PERMISSION);
        }
        ptRepository.deleteById(ptId);
    }

    public void updatePtSuggestion(
        String authorization,
        Long ptId,
        PtSuggestionUpdateRequest ptSuggestionUpdateRequest) {
        // 토큰 파싱하여 유저 정보 받아오기
        User user = new User();
        int status = ptSuggestionUpdateRequest.status();
        Pt suggestion = findSuggestion(ptId);
        if(!suggestion.getUser().equals(user))
            throw new PermissionException(NO_PERMISSION);
        ptRepository.save(updateStatus(suggestion, status));
    }

    private Pt updateStatus(Pt pt, int status) {
        if(status != 1 && status != 3) {
            throw new IllegalArgumentException();
        }
        if(status == 1) {
            pt.reject();
        }
        if(status == 3) {
            pt.accept();
        }
        return pt;
    }

    private Pt findSuggestion(Long ptId) {
        return ptRepository.findById(ptId)
            .orElseThrow(() -> new InvalidIdException(NOT_EXIST_ID));
    }
}
