package linkfit.repository;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import linkfit.component.DefaultImageProvider;
import linkfit.entity.Gym;
import linkfit.entity.Trainer;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class TrainerRepositoryTest {

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private GymRepository gymRepository;

    private Gym testGym;

    @Mock
    private DefaultImageProvider defaultImageProvider;

    @BeforeEach
    void setUp() {
        when(defaultImageProvider.getDefaultImageUrl()).thenReturn("default-image-url");
        testGym = new Gym("감자 헬스장","강원특별자치도 춘천시 백령로 234번길");
        gymRepository.save(testGym);

        Trainer trainer1 = new Trainer("trainer1@link.fit","password","name1", TrainerGender.MALE);
        Trainer trainer2 = new Trainer("trainer2@link.fit","password","name2", TrainerGender.MALE);

        trainer1.setGym(testGym);
        trainer2.setGym(testGym);

        trainerRepository.save(trainer1);
        trainerRepository.save(trainer2);
    }
    @Test
    @DisplayName("existsByEmail() 테스트")
    void existsByEmail() {
        //when
        boolean exists = trainerRepository.existsByEmail("trainer1@link.fit");
        boolean notExists = trainerRepository.existsByEmail("user@link.fit");

        //then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    void findByEmailTest() {
        //when
        Optional<Trainer> foundTrainer = trainerRepository.findByEmail("trainer1@link.fit");

        //then
        assertThat(foundTrainer.isPresent()).isTrue();
        assertThat(foundTrainer.get().getEmail()).isEqualTo("trainer1@link.fit");

    }

    @Test
    void findAllByGym() {
        //given
        PageRequest pageable = PageRequest.of(0,10);

        //when
        List<Trainer> trainers = trainerRepository.findAllByGym(testGym,pageable);

        //then
        assertThat(trainers.size()).isEqualTo(2);
        assertThat(trainers.get(0).getGym()).isEqualTo(testGym);
    }
}