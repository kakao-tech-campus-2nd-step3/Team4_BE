package linkfit.dto;

import linkfit.entity.ChattingRoom;
import linkfit.entity.Trainer;
import linkfit.entity.User;

public record ChattingRoomRegisterRequest(Long trainerId) {
    public ChattingRoom toEntity(User user, Trainer trainer){
        return new ChattingRoom(user,trainer);
    }
}
