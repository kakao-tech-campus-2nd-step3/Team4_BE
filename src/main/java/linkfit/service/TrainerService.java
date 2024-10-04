package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.DUPLICATE_EMAIL;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_TRAINER;

import java.util.List;
import java.util.Objects;
import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.dto.LoginRequest;
import linkfit.dto.TrainerProfileResponse;
import linkfit.dto.TrainerRegisterRequest;
import linkfit.entity.Trainer;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.repository.TrainerRepository;
import linkfit.util.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final CareerService careerService;
    private final JwtUtil jwtUtil;
    private final ImageUploadService imageUploadService;

    public TrainerService(TrainerRepository trainerRepository, CareerService careerService,
        JwtUtil jwtUtil, ImageUploadService imageUploadService) {
        this.trainerRepository = trainerRepository;
        this.careerService = careerService;
        this.jwtUtil = jwtUtil;
        this.imageUploadService = imageUploadService;
    }

    @Transactional
    public void register(TrainerRegisterRequest request, MultipartFile profileImage) {
        if (trainerRepository.existsByEmail(request.email())) {
            throw new DuplicateException(DUPLICATE_EMAIL);
        }
        Trainer trainer = request.toEntity();
        String imageUrl = imageUploadService.uploadProfileImage(profileImage);
        trainer.setProfileImageUrl(imageUrl);
        trainerRepository.save(trainer);
    }

    public String login(LoginRequest request) {
        Trainer trainer = trainerRepository.findByEmail(request.email())
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_TRAINER));
        trainer.validatePassword(request.password());
        return jwtUtil.generateToken(trainer.getId(), trainer.getEmail());
    }

    public List<CareerResponse> getCareers(String authorization) {
        Trainer trainer = getMyInfo(authorization);
        return careerService.getAllTrainerCareers(trainer.getId());
    }

    public void deleteCareer(String authorization, Long careerId) {
        Trainer trainer = getMyInfo(authorization);
        if (Objects.equals(trainer.getId(), careerService.findTrainerIdByCareerId(careerId))) {
            careerService.deleteCareer(careerId);
        }
    }

    public void addCareer(String authorization, CareerRequest request) {
        Trainer trainer = getMyInfo(authorization);
        careerService.addCareer(trainer, request);
    }

    public Trainer getMyInfo(String authorization) {
        Long trainerId = jwtUtil.parseToken(authorization);
        return getTrainer(trainerId);
    }

    private Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_TRAINER));
    }

    public List<CareerResponse> getCareersByTrainerId(Long trainerId) {
        return careerService.getAllTrainerCareers(trainerId);
    }

    public TrainerProfileResponse getProfile(Long trainerId) {
        Trainer trainer = getTrainer(trainerId);
        return trainer.toDto();
    }

    public TrainerProfileResponse getMyProfile(String authorization) {
        Trainer trainer = getMyInfo(authorization);
        return trainer.toDto();
    }
}
