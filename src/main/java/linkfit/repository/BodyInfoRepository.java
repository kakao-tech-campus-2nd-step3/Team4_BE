package linkfit.repository;

import java.util.Optional;
import linkfit.entity.BodyInfo;
import linkfit.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyInfoRepository extends JpaRepository<BodyInfo, Long> {

    Page<BodyInfo> findAllByUserId(Long userId, Pageable pageable);

    Optional<BodyInfo> findTopByUserOrderByCreateDate(User user);
}
