package linkfit.dto;

import java.util.List;
import linkfit.entity.Gym;

public record GymSearchResponse(List<Gym> gyms) {

}
