package linkfit.dto;

import java.util.List;

public record PtUserProfileResponse(Long userId, String userName, String profileImageUrl,
                                    int totalCount, List<ScheduleResponse> schedules) {

}