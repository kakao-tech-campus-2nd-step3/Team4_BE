package linkfit.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import linkfit.entity.Gym;
import linkfit.entity.GymImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class GymImageRepositoryTest {

    @Autowired
    private GymImageRepository gymImageRepository;

    @Autowired
    private TestEntityManager entityManager;

    Gym gym;
    GymImage gymImage;
    GymImage gymImage1;

    @BeforeEach
    void setUp() {
        gym = new Gym("GymName","Test Location");
        entityManager.persist(gym);
        entityManager.flush();

        gymImage = new GymImage(gym,"imageurl");
        gymImage1 = new GymImage(gym,"imageurl1");

        gymImageRepository.save(gymImage);
        gymImageRepository.save(gymImage1);
    }

    @Test
    @DisplayName("Gym Image 조회 테스트")
    void findAllByGym() {
        //when
        List<GymImage> gymImageList = gymImageRepository.findAllByGym(gym);

        //then
        assertNotNull(gymImageList);
        assertEquals(gymImageList.size(),2);

    }

    @Test
    @DisplayName("Gym image 모두 삭제 테스트")
    void deleteAllByGym() {
        //when
        gymImageRepository.deleteAllByGym(gym);
        List<GymImage> gymImageList = gymImageRepository.findAllByGym(gym);
        //then
        assertNotNull(gymImageList);
        assertEquals(gymImageList.size(),0);

    }
}