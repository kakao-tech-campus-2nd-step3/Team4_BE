package linkfit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import linkfit.entity.Pt;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.status.PtStatus;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class PtRepositoryTest {

    @Autowired
    private PtRepository ptRepository;

    @Autowired
    private TestEntityManager entityManager;

    Pt pt;
    Trainer trainer;
    User user;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("trainer@link.fit", "password", "트레이너1", TrainerGender.MALE);
        user = new User("user@link.fit", "password", "일반회원1", "강원도 춘천시");
        User user1 = new User("user1@link.fit", "password", "일반회원1", "강원도 춘천시");

        pt = new Pt(user,trainer,30,100, LocalDateTime.now(), PtStatus.APPROVAL);
        Pt pt1 = new Pt(user1,trainer,30,100, LocalDateTime.now(), PtStatus.REFUSE);

        entityManager.persist(trainer);
        entityManager.persist(user);
        entityManager.persist(user1);

        ptRepository.save(pt);
        ptRepository.save(pt1);

    }

    @Test
    @DisplayName("Trainer 객체로 PT 조회")
    void findAllByTrainer() {

        //when
        Page<Pt> ptList = ptRepository.findAllByTrainer(trainer, PageRequest.of(0,10));

        //then
        assertThat(ptList.getContent().size()).isEqualTo(2);
        assert(ptList.getContent().stream().allMatch(pt->pt.getTrainer().equals(trainer)));
    }

    @Test
    void findAllByTrainerAndStatus() {
        //when
        Page<Pt> ptList = ptRepository.findAllByTrainerAndStatus(trainer, PtStatus.APPROVAL,PageRequest.of(0,10));

        //then
        assertThat(ptList.getContent().size()).isEqualTo(1);
        assert(ptList.getContent().stream().allMatch(pt->pt.getTrainer().equals(trainer)));
    }

    @Test
    void findByUser() {
        //when
        List<Pt> ptList = ptRepository.findByUser(user);

        //then
        assertThat(ptList.size()).isEqualTo(1);
        assert(ptList.stream().allMatch(pt->pt.getUser().equals(user)));
    }

    @Test
    void findByUserAndStatus() {
        //when
        Optional<Pt> userPt = ptRepository.findByUserAndStatus(user, PtStatus.APPROVAL);

        //then
        assertTrue(userPt.isPresent());
        assertThat(userPt.get().getUser().equals(user)).isEqualTo(true);
    }

    @Test
    void findAllByUserAndStatus() {

        //when
        Page<Pt> ptList = ptRepository.findAllByUserAndStatus(user, PtStatus.APPROVAL,PageRequest.of(0,10));

        //then
        assertThat(ptList.getContent().size()).isEqualTo(1);
        assert(ptList.getContent().stream().allMatch(pt->pt.getUser().equals(user)));

    }
}