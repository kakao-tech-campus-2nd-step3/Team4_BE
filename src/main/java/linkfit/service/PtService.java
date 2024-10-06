package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_PT;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_TRAINER;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_USER;
import static linkfit.exception.GlobalExceptionHandler.NOT_OWNER;

import java.util.List;
import linkfit.dto.PtSuggestionRequest;
import linkfit.dto.ReceivePtSuggestResponse;
import linkfit.dto.SendPtSuggestResponse;
import linkfit.dto.ProgressPtDetailResponse;
import linkfit.dto.ProgressPtListResponse;
import linkfit.dto.UserPtResponse;
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
import linkfit.status.PtStatus;
import linkfit.util.JwtUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PtService {

    private final PtRepository ptRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final TrainerRepository trainerRepository;

    public PtService(
        PtRepository ptRepository,
        ScheduleRepository scheduleRepository,
        UserRepository userRepository,
        JwtUtil jwtUtil,
        TrainerRepository trainerRepository) {
        this.ptRepository = ptRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.trainerRepository = trainerRepository;
    }

    public List<ProgressPtListResponse> getTrainerProgressPt(
        String authorization,
        Pageable pageable) {
        Trainer trainer = getTrainer(authorization);
        return ptRepository.findAllByTrainer(trainer, pageable)
            .stream()
            .map(Pt::toProgressDto)
            .toList();
    }

    public UserPtResponse getMyPt(
        String authorization) {
        User user = getUser(authorization);
        Pt pt = ptRepository.findByUser(user)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new UserPtResponse(pt, schedules);
    }

    public void sendSuggestion(
        String authorization,
        PtSuggestionRequest ptSuggestionRequest) {
        Trainer trainer = getTrainer(authorization);
        User user = userRepository.findById(ptSuggestionRequest.userId())
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER));
        Pt suggestion = new Pt(
            user,
            trainer,
            ptSuggestionRequest.totalCount(),
            ptSuggestionRequest.price()
        );
        ptRepository.save(suggestion);
    }

    public List<SendPtSuggestResponse> getAllSendSuggestion(
        String authorization,
        Pageable pageable) {
        Trainer trainer = getTrainer(authorization);
        return ptRepository.findAllByTrainer(trainer, pageable).stream()
            .map(Pt::toSendDto)
            .toList();
    }

    public List<ReceivePtSuggestResponse> getAllReceiveSuggestion(String authorization,
        Pageable pageable) {
        User user = getUser(authorization);
        return ptRepository.findAllByUserAndStatus(user, PtStatus.WAITING, pageable).stream()
            .map(Pt::toReceiveDto)
            .toList();
    }

    public void approvalSuggestion(String authorization, Long ptId) {
        User user = getUser(authorization);
        Pt suggestion = findSuggestion(ptId);
        if (!suggestion.getUser().equals(user)) {
            throw new PermissionException(NOT_OWNER);
        }
        suggestion.approval();
        ptRepository.save(suggestion);
    }

    public void recallSuggestion(String authorization, Long ptId) {
        Trainer trainer = getTrainer(authorization);
        Pt suggestion = findSuggestion(ptId);
        if (!suggestion.getTrainer().equals(trainer)) {
            throw new PermissionException(NOT_OWNER);
        }
        suggestion.recall();
        ptRepository.save(suggestion);
    }

    public void refuseSuggestion(String authorization, Long ptId) {
        User user = getUser(authorization);
        Pt suggestion = findSuggestion(ptId);
        if (!suggestion.getUser().equals(user)) {
            throw new PermissionException(NOT_OWNER);
        }
        suggestion.refuse();
        ptRepository.save(suggestion);
    }

    private Pt findSuggestion(Long ptId) {
        return ptRepository.findById(ptId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_PT));
    }

    public ProgressPtDetailResponse getProgressUserDetails(String authorization, Long ptId) {
        Trainer trainer = getTrainer(authorization);
        Pt pt = findSuggestion(ptId);
        if (!pt.getTrainer().equals(trainer)) {
            throw new PermissionException(NOT_OWNER);
        }
        User user = pt.getUser();
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new ProgressPtDetailResponse(user.getId(), user.getName(), user.getProfileImageUrl(),
            schedules);
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
}