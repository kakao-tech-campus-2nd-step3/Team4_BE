package linkfit.service;

import java.util.List;
import linkfit.dto.CareerResponse;
import linkfit.dto.TrainerProfileResponse;
import linkfit.entity.Trainer;
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
            .orElseThrow(() -> new IllegalStateException("존재하지 않는 트레이너."));
        return new TrainerProfileResponse(trainer);
    }

    //Trainer Career 삭제
    public void deleteCareer(Long careerId) {
        careerService.deleteCareer(careerId);
    }

    //Trainer Career 등록
    public void addCareer(Trainer trainer, CareerResponse res) {
        careerService.addCareer(trainer, res);
    }

    //Trainer Career 조회
    public List<CareerResponse> getCareers(Long trainerId) {
        return careerService.getAllTrainerCareers(trainerId);
    }

    public Trainer findByEmail(String email){
        return trainerRepository.findByEmail(email);
    }


}