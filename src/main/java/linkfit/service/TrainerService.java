package linkfit.service;

import java.util.List;
import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.dto.TrainerProfileResponse;
import linkfit.entity.Trainer;
import linkfit.exception.NotFoundTrainerException;
import linkfit.repository.TrainerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final CareerService careerService;

    public TrainerService(TrainerRepository trainerRepository, CareerService careerService) {
        this.trainerRepository = trainerRepository;
        this.careerService = careerService;
    }

    //Trainer Profile 조회
    public TrainerProfileResponse getProfile(Long trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
            .orElseThrow(() -> new NotFoundTrainerException("Trainer not found"));
        return new TrainerProfileResponse(trainer);
    }

    //Trainer Career 삭제
    public void deleteCareer(Long careerId) {
        careerService.deleteCareer(careerId);
    }

    //Trainer Career 등록
    public void addCareer(Trainer trainer, CareerRequest req) {
        careerService.addCareer(trainer, req);
    }

    //Trainer Career 조회
    public List<CareerResponse> getCareers(Long trainerId) {
        return careerService.getAllTrainerCareers(trainerId);
    }

    public Trainer findByEmail(String email){
        return trainerRepository.findByEmail(email);
    }


}