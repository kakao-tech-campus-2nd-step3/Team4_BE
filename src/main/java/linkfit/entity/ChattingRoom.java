package linkfit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import linkfit.dto.ChattingRoomResponse;

@Entity
@Table(name = "CHATTING_ROOM_TB")
public class ChattingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
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

    public ChattingRoomResponse toUserDto() {
        return new ChattingRoomResponse(id, trainer.getId(), trainer.getProfileImageUrl());
    }

    public ChattingRoomResponse toTrainerDto() {
        return new ChattingRoomResponse(id, user.getId(), user.getProfileImageUrl());
    }
}