package linkfit.service;

import linkfit.dto.LoginRequest;
import linkfit.dto.TokenResponse;
import linkfit.dto.TrainerProfileResponse;
import linkfit.dto.TrainerRegisterRequest;
import linkfit.entity.Gym;
import linkfit.entity.Trainer;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.TrainerRepository;
import linkfit.status.Role;
import linkfit.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final GymService gymService;
    private final ImageUploadService imageUploadService;

    public TrainerService(TrainerRepository trainerRepository, JwtUtil jwtUtil,
        PasswordEncoder passwordEncoder, GymService gymService,
        ImageUploadService imageUploadService) {
        this.trainerRepository = trainerRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.gymService = gymService;
        this.imageUploadService = imageUploadService;
    }

    @Transactional
    public void register(TrainerRegisterRequest request, MultipartFile profileImage) {
        validateEmailAlreadyExist(request.email());
        String encodedPassword = passwordEncoder.encode(request.password());
        Trainer trainer = request.toEntity(encodedPassword);
        trainer.setProfileImageUrl(imageUploadService.uploadProfileImage(profileImage));
        trainerRepository.save(trainer);
    }

    public TokenResponse login(LoginRequest request) {
        Trainer trainer = getTrainerByEmail(request.email());
        if (!authenticateTrainer(trainer, request.password())) {
            throw new PermissionException("not.match.password");
        }
        return new TokenResponse(
            jwtUtil.generateToken(Role.TRAINER, trainer.getId(), trainer.getEmail()));
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

    private boolean authenticateTrainer(Trainer trainer, String rawPassword) {
        return passwordEncoder.matches(rawPassword, trainer.getPassword());
    }

    private void validateEmailAlreadyExist(String email) {
        if (trainerRepository.existsByEmail(email)) {
            throw new DuplicateException("already.exist.email");
        }
    }

    public void setTrainerGym(Long trainerId, Long gymId) {
        Trainer trainer = getTrainer(trainerId);
        Gym gym = gymService.getGymById(gymId);
        trainer.setGym(gym);
        trainerRepository.save(trainer);
    }
}
