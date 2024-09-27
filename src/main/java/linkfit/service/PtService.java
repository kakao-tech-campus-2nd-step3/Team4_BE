package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_PT;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_TRAINER;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_USER;
import static linkfit.exception.GlobalExceptionHandler.NO_PERMISSION;

import java.util.List;
import linkfit.dto.PtSuggestionRequest;
import linkfit.dto.PtSuggestionResponse;
import linkfit.dto.PtSuggestionUpdateRequest;
import linkfit.dto.PtTrainerResponse;
import linkfit.dto.PtUserResponse;
import linkfit.dto.TrainerPtResponse;
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
import linkfit.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<TrainerPtResponse> getAllPt(
        String authorization,
        Pageable pageable) {
        Trainer trainer = getTrainer(authorization);
        return ptRepository.findAllByTrainer(trainer, pageable)
            .stream()
            .map(Pt::getUser)
            .map(TrainerPtResponse::new)
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

    public void suggestPt(
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

    public List<PtSuggestionResponse> getAllPtSuggestion(
        String authorization,
        Pageable pageable) {
        Trainer trainer = getTrainer(authorization);
        return ptRepository.findAllByTrainer(trainer, pageable).stream()
            .map(Pt::toDto)
            .toList();
    }

    public void recallPtSuggestion(String authorization, Long ptId) {
        Trainer trainer = getTrainer(authorization);
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
        User user = getUser(authorization);
        int status = ptSuggestionUpdateRequest.status();
        Pt suggestion = findSuggestion(ptId);
        if(!suggestion.getUser().equals(user)) {
            throw new PermissionException(NO_PERMISSION);
        }
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
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_PT));
    }

    public PtTrainerResponse getPtTrainerProfile(String authorization, Long ptId) {
        User user = getUser(authorization);
        Pt pt = findSuggestion(ptId);
        if(!pt.getUser().equals(user)) {
            throw new PermissionException(NO_PERMISSION);
        }
        return new PtTrainerResponse(pt.getTrainer());
    }

    public PtUserResponse getPtUserProfile(String authorization, Long ptId) {
        Trainer trainer = getTrainer(authorization);
        Pt pt = findSuggestion(ptId);
        if(!pt.getTrainer().equals(trainer)) {
            throw new PermissionException(NO_PERMISSION);
        }
        return new PtUserResponse(pt.getUser());
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