package linkfit.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import linkfit.entity.Gym;
import linkfit.status.GymStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
class GymRepositoryTest {

    @Autowired
    GymRepository gymRepository;



    Gym refusedGym;

    @BeforeEach
    void setUp() {
        //APPROVAL 상태인 Gym은 더미데이터 활용

        refusedGym = new Gym("gymName1", "gym location");
        refusedGym.refuse();
        gymRepository.save(refusedGym);
    }

    @Test
    @DisplayName("Gym KeyWord 검색 테스트")
    void findAllByNameContainingAndStatus() {

        //when
        List<Gym> gymList = gymRepository.findAllByNameContainingAndStatus("백령",
            GymStatus.APPROVAL, PageRequest.of(0, 10)).getContent();

        //then
        assertEquals(1, gymList.size());
        assertTrue(gymList.getFirst().getName().contains("백령"));
    }

    @Test
    @DisplayName("Gym Status 검색")
    void testFindAllByStatus() {

        //when
        List<Gym> approvedGymList = gymRepository.findAllByStatus(GymStatus.APPROVAL);
        List<Gym> refusedGymList =gymRepository.findAllByStatus(GymStatus.REFUSE);

        //then
        assertEquals(2, approvedGymList.size());
        assertTrue(approvedGymList.stream().allMatch(gym -> gym.getStatus().equals(GymStatus.APPROVAL)));

        assertEquals(1, refusedGymList.size());
        assertTrue(approvedGymList.stream().allMatch(gym -> gym.getStatus().equals(GymStatus.APPROVAL)));


    }


    @Test
    @DisplayName("Gym Status 검색 : 페이징")
    void findAllByStatusPageable() {

        //when
        List<Gym> approvedGymList = gymRepository.findAllByStatus(GymStatus.APPROVAL, PageRequest.of(0, 10)).getContent();
        List<Gym> refusedGymList = gymRepository.findAllByStatus(GymStatus.REFUSE, PageRequest.of(0, 10)).getContent();

        //then
        assertEquals(2, approvedGymList.size());
        assertTrue(approvedGymList.stream().allMatch(gym -> gym.getStatus().equals(GymStatus.APPROVAL)));


        assertEquals(1, refusedGymList.size());
        assertTrue(approvedGymList.stream().allMatch(gym -> gym.getStatus().equals(GymStatus.APPROVAL)));
    }
}