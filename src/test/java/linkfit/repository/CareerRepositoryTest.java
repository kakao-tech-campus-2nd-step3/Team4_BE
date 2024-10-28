package linkfit.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import linkfit.entity.Career;
import linkfit.entity.Trainer;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CareerRepositoryTest {

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private TrainerRepository trainerRepository;


    Trainer trainer1;
    Trainer trainer2;

    @BeforeEach
    void setUp() {
        trainer1 = new Trainer("trainer1@link.fit","password","트레이너1", TrainerGender.MALE);
        Career career1 = new Career(trainer1,"커리어1");
        Career career2 = new Career(trainer1,"커리어2");

        trainer2 = new Trainer("trainer2@link.fit","password","트레이너1", TrainerGender.MALE);

        trainerRepository.saveAndFlush(trainer1);
        trainerRepository.saveAndFlush(trainer2);

        careerRepository.save(career1);
        careerRepository.save(career2);
    }

    @Test
    @DisplayName("커리어 조회 테스트")
    void findCareerTest() {
        //when
        List<Career> list = careerRepository.findAllByTrainer(trainer1);

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("커리어 조회 테스트: 커리어 없을때")
    void findNoneCareerTest() {
        //when
        List<Career> list = careerRepository.findAllByTrainer(trainer2);

        //then
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(0);
    }


}