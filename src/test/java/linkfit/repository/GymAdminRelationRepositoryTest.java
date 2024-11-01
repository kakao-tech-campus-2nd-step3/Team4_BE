package linkfit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.Null;
import java.util.Optional;
import linkfit.entity.Gym;
import linkfit.entity.GymAdminRelation;
import linkfit.entity.Trainer;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class GymAdminRelationRepositoryTest {


    @Autowired
    GymAdminRelationRepository gymAdminRelationRepository;

    @Autowired
    EntityManager entityManager;

    Gym gym;
    Trainer trainer;
    @BeforeEach
    void setUp() {
        gym = new Gym("GymName","Test Location");
        trainer = new Trainer("trainer@link.fit","password","트레이너", TrainerGender.MALE);

        entityManager.persist(gym);
        entityManager.persist(trainer);
        entityManager.flush();

        gymAdminRelationRepository.save(new GymAdminRelation(gym,trainer));

    }

    @Test
    @DisplayName("GymAdmin 삭제 테스트")
    void deleteByGym() {

        //when
        gymAdminRelationRepository.deleteByGym(gym);

        //then
        Optional<GymAdminRelation> gymAdminRelation = gymAdminRelationRepository.findByGym(gym);
        assertThat(gymAdminRelation).isEmpty();
    }

    @Test
    @DisplayName("GymAdmin find 테스트")
    void findByGym() {

        //when
        GymAdminRelation gymAdminRelation = gymAdminRelationRepository.findByGym(gym).get();

        //then
        assertThat(gymAdminRelation).isNotNull();
        assertThat(gymAdminRelation.getTrainer()).isEqualTo(trainer);
    }
}