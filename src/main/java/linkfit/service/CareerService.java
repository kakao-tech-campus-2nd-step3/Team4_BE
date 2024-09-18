package linkfit.service;

import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.entity.Career;
import linkfit.entity.Trainer;
import linkfit.repository.CareerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerService {

    private final CareerRepository careerRepository;

    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }


    public List<CareerResponse> getAllTrainerCareers(Long trainerId) {
        List<Career> careers = careerRepository.findAllByTrainerId(trainerId);
        return careers.stream().map(CareerResponse::new).toList();
    }

    public void addCareer(Trainer trainer, CareerRequest req) {
        Career career = new Career(trainer, req.getCareer());
    }

    public void deleteCareer(Long careerId) {
        Career career = careerRepository.findById(careerId)
            .orElseThrow(() -> new IllegalStateException("존재하지 않는 경력"));

        careerRepository.deleteById(careerId);
    }
}