package linkfit.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import linkfit.entity.BodyInfo;

@Repository
public interface BodyInfoRepository extends JpaRepository<BodyInfo, Long> {

    Page<BodyInfo> findAllByUserId(Long userId, Pageable pageable);

    Optional<BodyInfo> findByUserId(Long userId);




}
