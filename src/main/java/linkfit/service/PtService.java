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

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PtService {

    private final PtRepository ptRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final PreferenceRepository preferenceRepository;

    public PtService(PtRepository ptRepository, ScheduleRepository scheduleRepository,
        UserRepository userRepository, TrainerRepository trainerRepository,
        PreferenceRepository preferenceRepository) {
        this.ptRepository = ptRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.trainerRepository = trainerRepository;
        this.preferenceRepository = preferenceRepository;
    }

    public List<ProgressPtListResponse> getTrainerProgressPt(Long trainerId, Pageable pageable) {
        Trainer trainer = getTrainer(trainerId);
        return ptRepository.findAllByTrainerAndStatus(trainer, PtStatus.APPROVAL, pageable)
            .stream()
            .map(Pt::toProgressDto)
            .toList();
    }

    public UserPtResponse getMyPt(Long userId) {
        User user = getUser(userId);
        Pt pt = ptRepository.findByUserAndStatus(user, PtStatus.APPROVAL)
            .orElseThrow(() -> new NotFoundException("not.found.pt"));
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new UserPtResponse(pt, schedules);
    }

    public void sendSuggestion(Long trainerId, PtSuggestionRequest ptSuggestionRequest) {
        Trainer trainer = getTrainer(trainerId);
        User user = getUser(ptSuggestionRequest.userId());
        Pt suggestion = new Pt(user, trainer, ptSuggestionRequest);
        ptRepository.save(suggestion);
    }

    public List<SendPtSuggestResponse> getAllSendSuggestion(Long trainerId, Pageable pageable) {
        Trainer trainer = getTrainer(trainerId);
        return ptRepository.findAllByTrainer(trainer, pageable).stream()
            .map(Pt::toSendDto)
            .toList();
    }

    public List<ReceivePtSuggestResponse> getAllReceiveSuggestion(Long userId, Pageable pageable) {
        User user = getUser(userId);
        return ptRepository.findAllByUserAndStatus(user, PtStatus.WAITING, pageable).stream()
            .map(Pt::toReceiveDto)
            .toList();
    }

    public void approvalSuggestion(Long userId, Long ptId) {
        User user = getUser(userId);
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

    public void recallSuggestion(Long trainerId, Long ptId) {
        Trainer trainer = getTrainer(trainerId);
        Pt suggestion = findSuggestion(ptId);
        if (!suggestion.getTrainer().equals(trainer)) {
            throw new PermissionException("not.owner");
        }
        suggestion.recall();
        ptRepository.save(suggestion);
    }

    public void refuseSuggestion(Long userId, Long ptId) {
        User user = getUser(userId);
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

    public ProgressPtDetailResponse getProgressUserDetails(Long trainerId, Long ptId) {
        Trainer trainer = getTrainer(trainerId);
        Pt pt = findSuggestion(ptId);
        if (!pt.getTrainer().equals(trainer)) {
            throw new PermissionException("not.owner");
        }
        User user = pt.getUser();
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);
        return new ProgressPtDetailResponse(user.getId(), user.getName(), user.getProfileImageUrl(),
            schedules);
    }

    private Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    private boolean isProcessingPT(User user) {
        return ptRepository.findByUserAndStatus(user, PtStatus.APPROVAL).isPresent();
    }
}