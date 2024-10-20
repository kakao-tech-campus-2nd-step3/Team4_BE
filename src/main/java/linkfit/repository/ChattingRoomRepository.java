package linkfit.repository;

import java.util.List;
import linkfit.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    public List<ChattingRoom> findAllByUserId(Long userId);
    public List<ChattingRoom> findAllByTrainerId(Long trainerId);

}
