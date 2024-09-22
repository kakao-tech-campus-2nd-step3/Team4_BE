package linkfit.repository;

import linkfit.entity.UserBodyInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface UserBodyInfoRepository extends JpaRepository<UserBodyInfo, Long> {

    Page<UserBodyInfo> findAllByUserId(Long userId, Pageable pageable);
}
