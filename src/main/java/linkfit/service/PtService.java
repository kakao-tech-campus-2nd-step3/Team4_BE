package linkfit.service;

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
import linkfit.repository.PreferenceRepository;
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
    private final PreferenceRepository preferenceRepository;

    public PtService(PtRepository ptRepository, ScheduleRepository scheduleRepository,
        UserRepository userRepository, JwtUtil jwtUtil, TrainerRepository trainerRepository,
        PreferenceRepository preferenceRepository) {
        this.ptRepository = ptRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.trainerRepository = trainerRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public List<ProgressPtListResponse> getTrainerProgressPt(
        String authorization,
        Pageable pageable) {
        Trainer trainer = getTrainer(authorization);
        return ptRepository.findAllByTrainerAndStatus(trainer, PtStatus.APPROVAL, pageable)
            .stream()
            .map(Pt::toProgressDto)
            .toList();
    }

    public UserPtResponse getMyPt(
        String authorization) {
        User user = getUser(authorization);
        Pt pt = ptRepository.findByUserAndStatus(user, PtStatus.APPROVAL)
            .orElseThrow(() -> new NotFoundException("not.found.pt"));
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new UserPtResponse(pt, schedules);
    }

    public void sendSuggestion(
        String authorization,
        PtSuggestionRequest ptSuggestionRequest) {
        Trainer trainer = getTrainer(authorization);
        User user = userRepository.findById(ptSuggestionRequest.userId())
            .orElseThrow(() -> new NotFoundException("not.found.user"));
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
        List<Pt> ptList = ptRepository.findByUser(user);
        Pt suggestion = findSuggestion(ptId);
        if (!suggestion.getUser().equals(user)) {
            throw new PermissionException("not.owner");
        }
        if (isProcessingPT(user)) {
            throw new PermissionException("already.processing.pt");
        }
        ptList.forEach(pt -> {
            pt.refuse();
            ptRepository.save(pt);
        });
        suggestion.approval();
        preferenceRepository.deleteByUser(user);
        ptRepository.save(suggestion);
    }

    public void recallSuggestion(String authorization, Long ptId) {
        Trainer trainer = getTrainer(authorization);
        Pt suggestion = findSuggestion(ptId);
        if (!suggestion.getTrainer().equals(trainer)) {
            throw new PermissionException("not.owner");
        }
        suggestion.recall();
        ptRepository.save(suggestion);
    }

    public void refuseSuggestion(String authorization, Long ptId) {
        User user = getUser(authorization);
        Pt suggestion = findSuggestion(ptId);
        if (!suggestion.getUser().equals(user)) {
            throw new PermissionException("not.owner");
        }
        suggestion.refuse();
        ptRepository.save(suggestion);
    }

    private Pt findSuggestion(Long ptId) {
        return ptRepository.findById(ptId)
            .orElseThrow(() -> new NotFoundException("not.found.pt"));
    }

    public ProgressPtDetailResponse getProgressUserDetails(String authorization, Long ptId) {
        Trainer trainer = getTrainer(authorization);
        Pt pt = findSuggestion(ptId);
        if (!pt.getTrainer().equals(trainer)) {
            throw new PermissionException("not.owner");
        }
        User user = pt.getUser();
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new ProgressPtDetailResponse(user.getId(), user.getName(), user.getProfileImageUrl(),
            schedules);
    }

    private Trainer getTrainer(String authorization) {
        Long id = jwtUtil.parseToken(authorization);
        return trainerRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
    }

    private User getUser(String authorization) {
        Long id = jwtUtil.parseToken(authorization);
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    private boolean isProcessingPT(User user) {
        return ptRepository.findByUserAndStatus(user, PtStatus.APPROVAL).isPresent();
    }
}