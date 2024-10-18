package linkfit.dto;

import linkfit.entity.ChatRoomEntity;
import java.util.UUID;

public record ChatRoomDTO(String roomId, String name1, String name2) {

    // DTO -> Entity 변환 메서드
    public ChatRoomEntity toEntity() {
        return new ChatRoomEntity(roomId != null ? roomId : UUID.randomUUID().toString(), name1, name2);
    }
}
