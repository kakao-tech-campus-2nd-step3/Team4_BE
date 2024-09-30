package linkfit.service;

import java.util.List;
import linkfit.dto.GymRegisterRequest;
import linkfit.entity.Gym;
import linkfit.repository.GymRepository;
import org.springframework.stereotype.Service;

@Service
public class GymService {

    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    public List<Gym> findAllGym() {
        return gymRepository.findAll();
    }

    public void registerGym(GymRegisterRequest gymRegisterRequest) {
        gymRepository.save(gymRegisterRequest.toEntity());
    }
}
