package linkfit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import linkfit.dto.ChatRoomDTO;

@Entity
@Table(name = "chat_room")
public class ChatRoomEntity {

    @Id
    private String roomId;
    private String name1;
    private String name2;

    // 기본 생성자
    protected ChatRoomEntity() {}

    // 생성자
    public ChatRoomEntity(String roomId, String name1, String name2) {
        this.roomId = roomId;
        this.name1 = name1;
        this.name2 = name2;
    }

    // Getter
    public String getRoomId() {
        return roomId;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    // Entity -> DTO 변환 메서드
    public ChatRoomDTO toDto() {
        return new ChatRoomDTO(this.roomId, this.name1, this.name2);
    }
}
