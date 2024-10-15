package linkfit.service;

import linkfit.dto.*;
import linkfit.entity.Trainer;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.repository.TrainerRepository;
import linkfit.util.JwtUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final JwtUtil jwtUtil;

    public TrainerService(TrainerRepository trainerRepository, JwtUtil jwtUtil) {
        this.trainerRepository = trainerRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public void register(TrainerRegisterRequest request) {
        validateEmailAlreadyExist(request.email());
        Trainer trainer = request.toEntity();
        trainerRepository.save(trainer);
    }

    public TokenResponse login(LoginRequest request) {
        Trainer trainer = getTrainerByEmail(request.email());
        trainer.validatePassword(request.password());
        return new TokenResponse(jwtUtil.generateToken(trainer.getId(), trainer.getEmail()));
    }

    public Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
    }

    public TrainerProfileResponse getProfile(Long trainerId) {
        return getTrainer(trainerId).toDto();
    }

    public TrainerProfileResponse getMyProfile(Long trainerId) {
        return getProfile(trainerId);
    }

    private Trainer getTrainerByEmail(String email) {
        return trainerRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
    }

    private void validateEmailAlreadyExist(String email) {
        if (trainerRepository.existsByEmail(email)) {
            throw new DuplicateException("already.exist.email");
        }
    }


}