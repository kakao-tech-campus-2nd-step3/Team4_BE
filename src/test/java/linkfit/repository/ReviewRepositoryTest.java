package linkfit.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import linkfit.entity.Review;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TestEntityManager entityManager;
    Trainer trainer;
    User user;

    Review review;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("trainer@link.fit", "password", "트레이너1", TrainerGender.MALE);
        user = new User("user@link.fit", "password", "일반회원1", "강원도 춘천시");
        review = new Review(user, trainer, "리뷰 content", 5);

        entityManager.persist(trainer);
        entityManager.persist(user);
        entityManager.flush();

        reviewRepository.save(review);
    }

    @Test
    void findAllByTrainerId() {

        //when
        List<Review> reviews = reviewRepository.findAllByTrainerId(trainer.getId());

        //then
        assertEquals(1, reviews.size());
        assertEquals(review, reviews.get(0));
    }

    @Test
    void findAllByUserId() {
        //when
        List<Review> reviews = reviewRepository.findAllByUserId(user.getId());

        //then
        assertEquals(1, reviews.size());
        assertEquals(review, reviews.get(0));
    }
}