package linkfit.repository;

import linkfit.entity.BodyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserBodyInfoRepository extends JpaRepository<BodyInfo, Long> {

    Page<BodyInfo> findAllByUserId(Long userId, Pageable pageable);
}
