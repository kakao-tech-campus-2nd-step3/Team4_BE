package linkfit.service;

import java.util.List;

import org.springframework.stereotype.Service;

import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.dto.TrainerProfileResponse;
import linkfit.entity.Trainer;
import linkfit.exception.InvalidIdException;
import linkfit.exception.NotFoundTrainerException;
import linkfit.repository.TrainerRepository;
import linkfit.util.JwtUtil;

@Service
public class TrainerService extends PersonService<Trainer> {

    private TrainerRepository trainerRepository;
    private final CareerService careerService;
    private final JwtUtil jwtUtil;

    public TrainerService(TrainerRepository trainerRepository, CareerService careerService,
        JwtUtil jwtUtil) {
        super(trainerRepository);
        this.careerService = careerService;
        this.jwtUtil = jwtUtil;
    }

    //Trainer Career 조회
    public List<CareerResponse> getCareers(String authorization) {
        Trainer trainer = getTrainer(authorization);
        return careerService.getAllTrainerCareers(trainer.getId());
    }

    //Trainer Career 삭제
    public void deleteCareer(String authorization, Long careerId) {
        Trainer trainer = getTrainer(authorization);
        if (trainer.getId() == careerService.findTrainerIdByCareerId(careerId)) {
            careerService.deleteCareer(careerId);
        }
    }

    //Trainer Career 등록
    public void addCareer(String authorization, CareerRequest request) {
        Trainer trainer = getTrainer(authorization);
        careerService.addCareer(trainer, request);
    }

    public Trainer getTrainer(String authorization) {
        Long trainerId = jwtUtil.parseToken(authorization);
        Trainer trainer = trainerRepository.findById(trainerId)
            .orElseThrow(() -> new InvalidIdException("Trainer profile does not exist."));
        return trainer;
    }

    public List<CareerResponse> getCareersByTrainerId(Long trainerId) {
        return careerService.getAllTrainerCareers(trainerId);
    }

    //Trainer Profile 조회
    public TrainerProfileResponse getProfile(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundTrainerException("Trainer not found"));
        return new TrainerProfileResponse(trainer);
    }
}
