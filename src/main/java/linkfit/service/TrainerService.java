package linkfit.service;

import java.util.List;

import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.dto.LoginRequest;
import linkfit.dto.TrainerProfileResponse;
import linkfit.dto.TrainerRegisterRequest;
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
    public void register(TrainerRegisterRequest request, MultipartFile profileImage) {
        if (trainerRepository.existsByEmail(request.email())) {
            throw new DuplicateException("duplicate.email");
        }
        Trainer trainer = request.toEntity();
        String imageUrl = imageUploadService.uploadProfileImage(profileImage);
        trainer.setProfileImageUrl(imageUrl);
        trainerRepository.save(trainer);
    }

    public String login(LoginRequest request) {
        Trainer trainer = trainerRepository.findByEmail(request.email())
            .orElseThrow(() -> new NotFoundException("not.found.trainer"));
        trainer.validatePassword(request.password());
        return jwtUtil.generateToken(trainer.getId(), trainer.getEmail());
    }

    public List<CareerResponse> getCareers(Long trainerId) {
        Trainer trainer = getTrainer(trainerId);
        return careerService.getAllCareerByTrainer(trainer);
    }

    public void deleteCareer(Long trainerId, Long careerId) {
        Trainer trainer = getTrainer(trainerId);
        Career career = careerService.getCareer(careerId);
        validOwner(trainer, career);
        careerService.deleteCareer(careerId);
    }

    private void validOwner(Trainer trainer, Career career) {
        if (career.getTrainer() != trainer) {
            throw new PermissionException("career.permission.denied");
        }
    }

    public void addCareer(Long trainerId, List<CareerRequest> request) {
        Trainer trainer = getTrainer(trainerId);
        careerService.addCareer(trainer, request);
    }


    public void identifyTrainer(Long trainerId) {
        if (!trainerRepository.existsById(trainerId)) {
            throw new PermissionException("unregistered.trainer");
        }
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
        Trainer trainer = getTrainer(trainerId);
        return trainer.toDto();
    }

    public TrainerProfileResponse getMyProfile(Long trainerId) {
        return getProfile(trainerId);
    }
}
