package linkfit.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;
import linkfit.entity.Pt;
import linkfit.entity.Schedule;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.status.PtStatus;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class ScheduleRepositoryTest {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    TestEntityManager entityManager;


    Schedule schedule1;
    Schedule schedule2;
    Pt pt;
    @BeforeEach
    void setUp() {
        Trainer trainer = new Trainer("trainer@link.fit", "password", "트레이너1", TrainerGender.MALE);
        User user = new User("user@link.fit", "password", "일반회원1", "강원도 춘천시");
        pt = new Pt(user,trainer,10,200000, LocalDateTime.now(), PtStatus.APPROVAL);
        entityManager.persist(trainer);
        entityManager.persist(user);
        entityManager.persist(pt);

        schedule1 = new Schedule(pt,LocalDateTime.now());
        schedule2 = new Schedule(pt,LocalDateTime.now());

        scheduleRepository.save(schedule1);
        scheduleRepository.save(schedule2);
    }

    @Test
    void findAllByPt() {

        //when
        List<Schedule> schedules = scheduleRepository.findAllByPt(pt);

        //then
        assertEquals(2, schedules.size());
    }
}