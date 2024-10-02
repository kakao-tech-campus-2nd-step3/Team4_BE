package linkfit.dto;

import java.util.List;
import linkfit.entity.Gym;
import linkfit.entity.GymImage;

public record GymDetailResponse(Long id, String name, String location, List<String> gymImages) {

    public GymDetailResponse(Gym gym, List<GymImage> images) {
        this(
            gym.getId(),
            gym.getName(),
            gym.getLocation(),
            images.stream().map(GymImage::getImageUrl).toList()
        );
    }
}
