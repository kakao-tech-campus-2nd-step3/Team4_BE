package linkfit.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.GeneralPath;
import java.util.List;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class ChattingRoomRepositoryTest {

    @Autowired
    ChattingRoomRepository chattingRoomRepository;

    @Autowired
    TestEntityManager entityManager;

    Trainer trainer;
    User user;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("trainer@link.fit","password","트레이너1", TrainerGender.MALE);
        user = new User("user@link.fit","password","일반회원1","강원도 춘천시");

        entityManager.persist(trainer);
        entityManager.persist(user);

        entityManager.flush();

        ChattingRoom room = new ChattingRoom(user,trainer);
        chattingRoomRepository.save(room);
    }

    @Test
    @DisplayName("User Id로 채팅방 찾기")
    void findAllByUserId() {
        //when
        List<ChattingRoom> rooms = chattingRoomRepository.findAllByUser(user);

        //then
        assertEquals(1,rooms.size());
        rooms.forEach(room -> assertEquals(room.getUser(),user));

    }

    @Test
    @DisplayName("Trainer Id로 채팅방 찾기")
    void findAllByTrainerId() {
        //when
        List<ChattingRoom> rooms = chattingRoomRepository.findAllByTrainer(trainer);

        //then
        assertEquals(1,rooms.size());
        rooms.forEach(room -> assertEquals(room.getTrainer(),trainer));
    }
}