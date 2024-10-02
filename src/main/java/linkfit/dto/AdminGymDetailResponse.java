package linkfit.dto;

import java.util.List;
import linkfit.entity.Gym;
import linkfit.entity.Trainer;

public record AdminGymDetailResponse(Gym gym, List<Trainer> trainerList) {

}
