package linkfit.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import linkfit.entity.BodyInfo;
import linkfit.entity.Preference;
import linkfit.entity.Sports;
import linkfit.entity.User;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class PreferenceRepositoryTest {

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private TestEntityManager entityManager;

    User user;
    Preference preference;

    @BeforeEach
    void setUp() {

        user = new User("user@link.fit", "password", "일반회원1", "강원도 춘천시");
        BodyInfo bodyInfo = new BodyInfo(user,"imageurl1");
        Sports sports = new Sports("PT");
        preference = new Preference(user,bodyInfo,sports, TrainerGender.MALE,10,"10kg 감량");
        entityManager.persist(user);
        entityManager.persist(bodyInfo);
        entityManager.persist(sports);
        entityManager.persist(preference);
    }

    @Test
    @DisplayName("선호도 삭제 테스트")
    void deleteByUser() {
        //when
        preferenceRepository.deleteByUser(user);
        List<Preference> list= preferenceRepository.findAll();

        //then
        assertEquals(0,list.size());

    }
}