package linkfit.dto;

import linkfit.entity.User;

public class TrainerPtResponse {

    private Long userId;
    private String userName;
    private String userProfileImageUrl;
    private String userGoal;

    public TrainerPtResponse(User user) {
        this.userId = user.getId();
        this.userName = user.getName();
        this.userProfileImageUrl = user.getProfileImageUrl();
        this.userGoal = null;
    }

    public TrainerPtResponse(User user, String userGoal) {
        this.userId = user.getId();
        this.userName = user.getName();
        this.userProfileImageUrl = user.getProfileImageUrl();
        this.userGoal = userGoal;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public String getUserGoal() {
        return userGoal;
    }
}
