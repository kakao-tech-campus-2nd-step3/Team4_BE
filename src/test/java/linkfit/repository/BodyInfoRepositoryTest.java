package linkfit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.List;
import linkfit.entity.BodyInfo;
import linkfit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class BodyInfoRepositoryTest {

    @Autowired
    private BodyInfoRepository bodyInfoRepository;

    @Autowired
    private TestEntityManager entityManager;


    User user;
    @BeforeEach
    void setUp() {
        user = new User("user@link.fit","password","defaultImageUrl","강원도 춘천시 백령로 12333번길");
        entityManager.persist(user);
        entityManager.flush();

        BodyInfo bodyInfo = new BodyInfo(user,"imageurl1");
        BodyInfo bodyInfo1 = new BodyInfo(user,"imageurl2");
        BodyInfo bodyInfo2 = new BodyInfo(user,"imageurl3");
        bodyInfoRepository.save(bodyInfo);
        bodyInfoRepository.save(bodyInfo1);
        bodyInfoRepository.save(bodyInfo2);

    }

    @Test
    void findAllByUserId() {
        //when
        Page<BodyInfo> bodyInfos = bodyInfoRepository.findAllByUser(user,
            PageRequest.of(0, 10));

        //then
        assertThat(bodyInfos).isNotNull();
        assertThat(bodyInfos.getContent().size()).isEqualTo(3);

    }

    @Test
    void findTopByUserOrderByCreateDate() {
        BodyInfo bodyInfo = bodyInfoRepository.findTopByUserOrderByCreateDateDesc(user).get();

        assertThat(bodyInfo).isNotNull();
        assertThat(bodyInfo.getInbodyImageUrl()).isEqualTo("imageurl3");
    }
}