package linkfit.dto;

import java.util.List;

public record PtUserProfileResponse(Long userId, String userName, String profileImageUrl, List<ScheduleResponse> schedules) {

}