package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.CARRER_PERMISSION_DENIED;
import static linkfit.exception.GlobalExceptionHandler.DUPLICATE_EMAIL;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_CAREER;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_TRAINER;
import static linkfit.exception.GlobalExceptionHandler.UNREGISTERED_TRAINER;

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
import linkfit.repository.CareerRepository;
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
    private final CareerRepository careerRepository;

    public TrainerService(TrainerRepository trainerRepository, CareerService careerService,
        JwtUtil jwtUtil, ImageUploadService imageUploadService, CareerRepository careerRepository) {
        this.trainerRepository = trainerRepository;
        this.careerService = careerService;
        this.jwtUtil = jwtUtil;
        this.imageUploadService = imageUploadService;
        this.careerRepository = careerRepository;
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
        Long trainerId = identifyTrainer(authorization);
        Trainer trainer = getTrainer(trainerId);
        return careerService.getAllCareerByTrainer(trainer);
    }

    public void deleteCareer(String authorization, Long careerId) {
        Long trainerId = identifyTrainer(authorization);
        Trainer trainer = getTrainer(trainerId);
        Career career = careerService.getCareer(careerId);
        validOwner(trainer, career);
        careerService.deleteCareer(careerId);
    }

    private void validOwner(Trainer trainer, Career career) {
        if(career.getTrainer() != trainer)
            throw new PermissionException(CARRER_PERMISSION_DENIED);
    }

    public void addCareer(String authorization, CareerRequest request) {
        Long trainerId = identifyTrainer(authorization);
        Trainer trainer = getTrainer(trainerId);
        careerService.addCareer(trainer, request);
    }

    public Long identifyTrainer(String authorization) {
        Long trainerId =  jwtUtil.parseToken(authorization);
        if(!trainerRepository.existsById(trainerId))
            throw new PermissionException(UNREGISTERED_TRAINER);
        return trainerId;
    }

    public Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_TRAINER));
    }

    public List<CareerResponse> getCareersByTrainerId(Long trainerId) {
        Trainer trainer = getTrainer(trainerId);
        return careerService.getAllCareerByTrainer(trainer);
    }

    public TrainerProfileResponse getProfile(Long trainerId) {
        Trainer trainer = getTrainer(trainerId);
        return trainer.toDto();
    }

    public TrainerProfileResponse getMyProfile(String authorization) {
        Long trainerId = identifyTrainer(authorization);
        return getProfile(trainerId);
    }
}
