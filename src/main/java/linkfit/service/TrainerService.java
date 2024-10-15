package linkfit.service;

import java.util.List;

import java.util.Objects;

import linkfit.dto.*;
import linkfit.entity.Career;
import linkfit.entity.Trainer;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
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

    public List<CareerResponse> getCareers(Long trainerId) {
        Trainer trainer = getTrainer(trainerId);
        return careerService.getAllCareerByTrainer(trainer);
    }

    public void deleteCareer(Long trainerId, Long careerId) {
        Career career = careerService.getCareer(careerId);
        validateCareerOwnership(career, trainerId);
        careerService.deleteCareer(careerId);
    }

    public void addCareer(Long trainerId, List<CareerRequest> request) {
        Trainer trainer = getTrainer(trainerId);
        careerService.addCareer(trainer, request);
    }

    public Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
    }

    public List<CareerResponse> getCareersByTrainerId(Long trainerId) {
        Trainer trainer = getTrainer(trainerId);
        return careerService.getAllCareerByTrainer(trainer);
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

    private void handleProfileImage(MultipartFile profileImage, Trainer trainer) {
        String imageUrl = imageUploadService.uploadProfileImage(profileImage);
        if(imageUrl != null) {
            trainer.setProfileImageUrl(imageUrl);
        }
    }

    private void validateEmailAlreadyExist(String email) {
        if (trainerRepository.existsByEmail(email)) {
            throw new DuplicateException("already.exist.email");
        }
    }

    private void validateCareerOwnership(Career career, Long trainerId) {
        Long ownerId = career.getTrainer().getId();
        if (!Objects.equals(ownerId, trainerId)) {
            throw new PermissionException("career.permission.denied");
        }
    }
}