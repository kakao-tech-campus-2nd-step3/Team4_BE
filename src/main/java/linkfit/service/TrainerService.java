package linkfit.service;

import linkfit.dto.LoginRequest;
import linkfit.dto.TokenResponse;
import linkfit.dto.TrainerProfileResponse;
import linkfit.dto.TrainerRegisterRequest;
import linkfit.entity.Trainer;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.TrainerRepository;
import linkfit.util.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public TrainerService(TrainerRepository trainerRepository, CareerService careerService,
        JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(TrainerRegisterRequest request) {
        validateEmailAlreadyExist(request.email());
        String encodedPassword = passwordEncoder.encode(request.password());
        Trainer trainer = request.toEntity(encodedPassword);
        trainerRepository.save(trainer);
    }

    public TokenResponse login(LoginRequest request) {
        Trainer trainer = getTrainerByEmail(request.email());
        if (!trainerAuthenticate(trainer, request.password())) {
            throw new PermissionException("not.match.password");
        }
        return new TokenResponse(
            jwtUtil.generateToken("trainer", trainer.getId(), trainer.getEmail()));
    }

    public Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
    }

    public TrainerProfileResponse getProfile(Long trainerId) {
        return getTrainer(trainerId).toDto();
    }

    private Trainer getTrainerByEmail(String email) {
        return trainerRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("not.found.user"));
    }

    private boolean trainerAuthenticate(Trainer trainer, String rawPassword) {
        return passwordEncoder.matches(rawPassword, trainer.getPassword());
    }

    private void validateEmailAlreadyExist(String email) {
        if (trainerRepository.existsByEmail(email)) {
            throw new DuplicateException("already.exist.email");
        }
    }


}
