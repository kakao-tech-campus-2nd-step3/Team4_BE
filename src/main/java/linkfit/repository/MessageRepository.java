package linkfit.repository;

import java.util.List;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByChattingRoomOrderBySendTime(ChattingRoom room);

}
