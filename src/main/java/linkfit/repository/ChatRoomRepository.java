package linkfit.repository;

import java.util.Optional;
import linkfit.dto.ChatRoomDTO;
import linkfit.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {

    Optional<ChatRoomEntity> findById(String s);
}
