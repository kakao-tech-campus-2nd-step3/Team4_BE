package linkfit.dto;

import linkfit.status.Role;

public record ChattingRoomResponse(Long id, String memberName, String memberProfileImageUrl, String lastMessage,
                                   Role lastSender) {

}