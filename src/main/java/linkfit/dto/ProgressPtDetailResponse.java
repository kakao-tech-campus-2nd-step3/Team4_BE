package linkfit.dto;

import java.util.List;
import linkfit.entity.Schedule;

public record ProgressPtDetailResponse(Long userId, String userName, String userProfileImageUrl,
                                       List<Schedule> schedules) {

}