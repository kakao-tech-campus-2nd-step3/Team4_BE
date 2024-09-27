package linkfit.dto;

import linkfit.entity.User;

public record PtUserResponse(Long userId, String userName, String userProfileImageUrl) {
    public PtUserResponse(User user) {
        this(user.getId(), user.getName(), user.getProfileImageUrl());
    }
}
