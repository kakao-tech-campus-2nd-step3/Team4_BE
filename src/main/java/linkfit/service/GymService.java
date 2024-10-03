package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_GYM;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_RELATION;
import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_TRAINER;
import static linkfit.status.GymStatus.APPROVAL;
import static linkfit.status.GymStatus.WAITING;

import java.util.List;
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
import linkfit.repository.GymAdminRelationRepository;
import linkfit.repository.GymImageRepository;
import linkfit.repository.GymRepository;
import linkfit.repository.TrainerRepository;
import linkfit.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GymService {

    private final GymRepository gymRepository;
    private final TrainerRepository trainerRepository;
    private final GymImageRepository gymImageRepository;
    private final GymAdminRelationRepository gymAdminRelationRepository;
    private final JwtUtil jwtUtil;

    public GymService(GymRepository gymRepository, TrainerRepository trainerRepository,
        GymImageRepository gymImageRepository,
        GymAdminRelationRepository gymAdminRelationRepository, JwtUtil jwtUtil) {
        this.gymRepository = gymRepository;
        this.trainerRepository = trainerRepository;
        this.gymImageRepository = gymImageRepository;
        this.gymAdminRelationRepository = gymAdminRelationRepository;
        this.jwtUtil = jwtUtil;
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
            .map(gym -> new GymLocationResponse(gym.getId(),
                gym.getLocation()))  // Assuming GymLocationResponse has these fields
            .toList();
    }

    public void sendGymRegistrationRequest(String authorization, GymRegisterRequest gymRegisterRequest) {
        Trainer trainer = getTrainer(authorization);
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

    private Trainer getTrainer(String authorization) {
        Long trainerId = jwtUtil.parseToken(authorization);
        return trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_TRAINER));
    }

}
