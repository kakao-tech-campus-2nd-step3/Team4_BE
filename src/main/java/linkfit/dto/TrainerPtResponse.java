package linkfit.dto;

import linkfit.entity.User;

public record TrainerPtResponse(Long userId, String userName, String userProfileImageUrl,
                                String userGoal) {

    public TrainerPtResponse(User user) {
        this(user.getId(), user.getName(), user.getProfileImageUrl(), null);
    }

    public TrainerPtResponse(User user, String userGoal) {
        this(user.getId(), user.getName(), user.getProfileImageUrl(), userGoal);
    }
}