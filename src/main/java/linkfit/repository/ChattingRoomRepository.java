package linkfit.repository;

import java.util.List;
import linkfit.entity.ChattingRoom;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    List<ChattingRoom> findAllByUser(User user);
    List<ChattingRoom> findAllByTrainer(Trainer trainer);
    boolean existsByUserAndTrainer(User user, Trainer trainer);
    ChattingRoom findByUserAndTrainer(User user, Trainer trainer);

}
