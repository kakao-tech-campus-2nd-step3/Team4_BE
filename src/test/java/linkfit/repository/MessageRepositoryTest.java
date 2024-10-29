package linkfit.repository;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Message;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.status.Role;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    ChattingRoom chattingRoom;

    @BeforeEach
    void setUp() {
        Trainer trainer = new Trainer("trainer@link.fit", "password", "트레이너1", TrainerGender.MALE);
        User user = new User("user@link.fit", "password", "일반회원1", "강원도 춘천시");
        chattingRoom = new ChattingRoom(user, trainer);
        testEntityManager.persist(trainer);
        testEntityManager.persist(user);
        testEntityManager.persist(chattingRoom);
        testEntityManager.flush();

        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 20; i++) {
            Message message = new Message(chattingRoom, "content" + i,
                i % 2 == 0 ? Role.USER : Role.TRAINER, now);
            testEntityManager.persist(message);
            now = now.plusMinutes(1);
        }
    }

    @Test
    @DisplayName("채팅방 별 메시지 조회 테스트")
    void findAllByChattingRoomOrderBySendTime() {
        //when
        List<Message> messageList = messageRepository.findAllByChattingRoomOrderBySendTime(
            chattingRoom);

        //then
        assertEquals(20, messageList.size());
        assertEquals("content0", messageList.getFirst().getContent());
        assertEquals(Role.USER, messageList.getFirst().getSender());
        assertEquals("content19", messageList.getLast().getContent());
        assertEquals(Role.TRAINER, messageList.getLast().getSender());

    }
}