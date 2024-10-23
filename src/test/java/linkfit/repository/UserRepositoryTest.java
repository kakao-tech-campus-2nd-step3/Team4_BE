package linkfit.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import linkfit.component.DefaultImageProvider;
import linkfit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Mock
    private DefaultImageProvider defaultImageProvider;

    @BeforeEach
    void setUp() {
        when(defaultImageProvider.getDefaultImageUrl()).thenReturn("default-image-url");

        User user1 = new User("user1@link.fit","password","name1", "강원특별자치도 춘천시 백령로 12번길");
        User user2 = new User("user2@link.fit","password","name2", "강원특별자치도 춘천시 백령로 23번길");

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @DisplayName("existsByEmail() 테스트")
    void existsByEmail() {

        //when
        boolean exists = userRepository.existsByEmail("user1@link.fit");
        boolean notExists = userRepository.existsByEmail("user@link.fit");

        //then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("findByEmail() 테스트")
    void findByEmailTest() {
        //when
        Optional<User> foundUser = userRepository.findByEmail("user1@link.fit");

        //then
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getEmail()).isEqualTo("user1@link.fit");

    }

}