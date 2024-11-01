package linkfit.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class SportsRepositoryTest {

    @Autowired
    private SportsRepository sportsRepository;

    @BeforeEach
    void setUp() {
        //더미데이터 활용
        // PT: 존재하는 Sports Name
        // CrossFit: 존재하지 않는 Sports Name
    }

    @Test
    void existsByName() {

        //when
        boolean existed = sportsRepository.existsByName("PT");
        boolean notExisted = sportsRepository.existsByName("CrossFit");

        //then
        assertTrue(existed);
        assertFalse(notExisted);

    }
}