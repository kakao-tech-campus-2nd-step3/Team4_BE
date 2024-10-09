package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.GYM_ADMIN_PERMISSION_DENIED;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_GYM;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_RELATION;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_TRAINER;
import static linkfit.status.GymStatus.APPROVAL;
import static linkfit.status.GymStatus.WAITING;

import java.util.List;
import linkfit.dto.GymDescriptionRequest;
import linkfit.dto.GymDetailResponse;
import linkfit.dto.GymLocationResponse;
import linkfit.dto.GymRegisterRequest;
import linkfit.dto.GymRegisterWaitingResponse;
import linkfit.dto.GymSearchResponse;
import linkfit.dto.GymTrainersResponse;
import linkfit.entity.Gym;
import linkfit.entity.GymAdminRelation;
import linkfit.entity.GymImage;
import linkfit.entity.Trainer;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.GymAdminRelationRepository;
import linkfit.repository.GymImageRepository;
import linkfit.repository.GymRepository;
import linkfit.repository.TrainerRepository;
import linkfit.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class GymService {

    private final GymRepository gymRepository;
    private final TrainerRepository trainerRepository;
    private final GymImageRepository gymImageRepository;
    private final GymAdminRelationRepository gymAdminRelationRepository;
    private final JwtUtil jwtUtil;
    private final ImageUploadService imageUploadService;

    public GymService(GymRepository gymRepository, TrainerRepository trainerRepository,
        GymImageRepository gymImageRepository,
        GymAdminRelationRepository gymAdminRelationRepository, JwtUtil jwtUtil,
        ImageUploadService imageUploadService) {
        this.gymRepository = gymRepository;
        this.trainerRepository = trainerRepository;
        this.gymImageRepository = gymImageRepository;
        this.gymAdminRelationRepository = gymAdminRelationRepository;
        this.jwtUtil = jwtUtil;
        this.imageUploadService = imageUploadService;
    }

    public List<Gym> findAllGym(Pageable pageable) {
        return gymRepository.findAllByStatus(APPROVAL, pageable)
            .stream().toList();
    }

    public void approvalGym(Long gymId) {
        Gym gym = getGymById(gymId);
        gym.approval();
        gymRepository.save(gym);
    }

    public void deleteGym(Long gymId) {
        Gym gym = getGymById(gymId);
        gym.refuse();
        gymRepository.save(gym);
        gymAdminRelationRepository.deleteByGym(gym);
    }

    public GymSearchResponse findAllByKeyword(String keyword, Pageable pageable) {
        Page<Gym> gymList = gymRepository.findAllByNameContainingAndStatus(keyword, APPROVAL,
            pageable);
        return new GymSearchResponse(gymList.getContent());
    }

    public GymDetailResponse getGymDetails(Long gymId) {
        Gym gym = getGymById(gymId);
        List<GymImage> images = gymImageRepository.findAllByGym(gym);
        return new GymDetailResponse(gym, images);
    }

    public Gym getGymById(Long id) {
        return gymRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_GYM));
    }

    public GymTrainersResponse getGymTrainers(Long gymId, Pageable pageable) {
        Gym gym = getGymById(gymId);
        List<Trainer> trainerList = trainerRepository.findAllByGym(gym, pageable);
        return new GymTrainersResponse(trainerList);
    }

    public List<GymLocationResponse> getGymLocations() {
        List<Gym> gymList = gymRepository.findAll();
        return gymList.stream()
            .map(gym -> new GymLocationResponse(
                gym.getId(), gym.getLocation()))
            .toList();
    }

    public void sendGymRegistrationRequest(Long trainerId,
        GymRegisterRequest gymRegisterRequest) {
        Trainer trainer = getTrainer(trainerId);
        Gym gym = gymRegisterRequest.toEntity();
        gymRepository.save(gym);
        GymAdminRelation gymAdminRelation = new GymAdminRelation(gym, trainer);
        gymAdminRelationRepository.save(gymAdminRelation);
    }

    public List<GymRegisterWaitingResponse> getGymRegisterWaitingList() {
        return gymRepository.findAllByStatus(WAITING)
            .stream()
            .map(Gym::toDTO)
            .toList();
    }

    private Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_TRAINER));
    }

    public void updateGym(Long gymId, Long trainerId,
        GymDescriptionRequest gymDescriptionRequest, List<MultipartFile> gymImages) {
        Trainer trainer = getTrainer(trainerId);
        Gym gym = getGymById(gymId);
        validPermission(gym, trainer);
        updateDescription(gym, gymDescriptionRequest.description());
        updateImages(gym, gymImages);
    }

    private void validPermission(Gym gym, Trainer trainer) {
        GymAdminRelation gymAdminRelation = gymAdminRelationRepository.findByGym(gym)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_RELATION));
        if (!trainer.equals(gymAdminRelation.getTrainer())) {
            throw new PermissionException(GYM_ADMIN_PERMISSION_DENIED);
        }
    }

    private void updateDescription(Gym gym, String description) {
        gym.updateGym(description);
        gymRepository.save(gym);
    }

    private void updateImages(Gym gym, List<MultipartFile> gymImages) {
        gymImageRepository.deleteAllByGym(gym);
        for (MultipartFile image : gymImages) {
            String imageUrl = imageUploadService.saveImage(image);
            GymImage gymImage = new GymImage(gym, imageUrl);
            gymImageRepository.save(gymImage);
        }
    }
}
