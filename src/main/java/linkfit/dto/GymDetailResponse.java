package linkfit.dto;

import java.util.List;

public record GymDetailResponse(Long id, String name, String location, String description,
                                List<String> images) {

}
