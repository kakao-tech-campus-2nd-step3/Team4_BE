package linkfit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "MESSAGE_TB")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChattingRoom chattingRoom;

    private String content;

    //Enum값으로 변경 필요
    private String sender;

    private LocalDateTime sendTime;

    protected Message() {
    }

    public Message(ChattingRoom chattingRoom, String content, String sender,
        LocalDateTime sendTime) {
        this.chattingRoom = chattingRoom;
        this.content = content;
        this.sender = sender;
        this.sendTime = sendTime;
    }

    public ChattingRoom getChattingRoom() {
        return chattingRoom;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }
}
