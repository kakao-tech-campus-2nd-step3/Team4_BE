package linkfit.entity;

import jakarta.persistence.*;
import linkfit.dto.ChattingRoomResponse;

@Entity
@Table(name = "CHATTING_ROOM_TB")
public class ChattingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    protected ChattingRoom() {
    }

    public ChattingRoom(User user, Trainer trainer) {
        this.user = user;
        this.trainer = trainer;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public ChattingRoomResponse toUserDto(Message message) {
        if(message == null) {
            return new ChattingRoomResponse(id, trainer.getName(), trainer.getProfileImageUrl(), null, null);
        }
        return new ChattingRoomResponse(id, trainer.getName(), trainer.getProfileImageUrl(), message.getContent(), message.getSender());
    }

    public ChattingRoomResponse toTrainerDto(Message message) {
        if(message == null) {
            return new ChattingRoomResponse(id, user.getName(), user.getProfileImageUrl(), null, null);
        }
        return new ChattingRoomResponse(id, user.getName(), user.getProfileImageUrl(), message.getContent(), message.getSender());
    }
}