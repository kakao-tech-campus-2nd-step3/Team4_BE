package linkfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import linkfit.dto.MessageResponse;
import linkfit.status.Role;

@Entity
@Table(name = "MESSAGE_TB")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ChattingRoom chattingRoom;

    @Column(nullable = false)
    private String content;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role sender;

    @Column(nullable = false)
    private LocalDateTime sendTime;

    protected Message() {
    }

    public Message(ChattingRoom chattingRoom, String content, Role sender,
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

    public Role getSender() {
        return sender;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public MessageResponse toDto() {
        return new MessageResponse(this.id, this.content, this.sender, this.sendTime);
    }
}
