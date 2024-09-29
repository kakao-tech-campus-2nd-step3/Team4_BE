package linkfit.repository;

import java.util.List;

import linkfit.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByTrainerId(Long id);

    List<Review> findAllByUserId(Long id);
}
