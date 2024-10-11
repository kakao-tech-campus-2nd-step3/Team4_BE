package linkfit.dto;

import linkfit.entity.User;

public record ProgressPtListResponse(Long id, Long userId, String userName, String profileImageUrl) {
}