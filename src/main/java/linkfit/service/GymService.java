package linkfit.service;

import static linkfit.exception.GlobalExceptionHandler.NOT_FOUND_GYM;

import java.util.List;
import linkfit.dto.GymRegisterRequest;
import linkfit.entity.Gym;
import linkfit.exception.NotFoundException;
import linkfit.repository.GymRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GymService {

    private final GymRepository gymRepository;

    public GymService(GymRepository gymRepository) {
        this.gymRepository = gymRepository;
    }

    public List<Gym> findAllGym(Pageable pageable) {
        return gymRepository.findAll(pageable).stream().toList();
    }

    public void registerGym(GymRegisterRequest gymRegisterRequest) {
        gymRepository.save(gymRegisterRequest.toEntity());
    }

    public void deleteGym(Long gymId) {
        if(!gymRepository.existsById(gymId))
            throw new NotFoundException(NOT_FOUND_GYM);
        gymRepository.deleteById(gymId);
    }
}
