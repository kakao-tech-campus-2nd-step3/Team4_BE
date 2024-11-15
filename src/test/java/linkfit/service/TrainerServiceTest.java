package linkfit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import linkfit.component.DefaultImageProvider;
import linkfit.dto.LoginRequest;
import linkfit.dto.TokenResponse;
import linkfit.dto.TrainerProfileResponse;
import linkfit.dto.TrainerRegisterRequest;
import linkfit.dto.UserRegisterRequest;
import linkfit.entity.Trainer;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.TrainerRepository;
import linkfit.status.Role;
import linkfit.status.TrainerGender;
import linkfit.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DefaultImageProvider defaultImageProvider;

    @InjectMocks
    private TrainerService trainerService;

    Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer("trainer@link.fit","password","trainer", TrainerGender.MALE);
    }

    @Test
    @DisplayName("register() 테스트 : 성공")
    void registerSucceed() {
        //given
        TrainerRegisterRequest request = new TrainerRegisterRequest("user@link.fit", "password", "name",
            TrainerGender.MALE);
        when(trainerRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("password");
        when(defaultImageProvider.getDefaultImageUrl()).thenReturn("defaultImageUrl");

        //when
        trainerService.register(request, null);

        //then
        verify(trainerRepository,times(1)).save(any(Trainer.class));

    }

    @Test
    @DisplayName("register() 테스트 : 중복 이메일")
    void registerFailed() {
        //given
        TrainerRegisterRequest request = new TrainerRegisterRequest("user@link.fit", "password", "name",
            TrainerGender.MALE);
        when(trainerRepository.existsByEmail(any())).thenReturn(true);

        //when & then
       assertThrows(DuplicateException.class, () -> trainerService.register(request, null));

    }

    @Test
    @DisplayName("login() 테스트 : 성공")
    void loginSucceed() {
        //given
        LoginRequest request = new LoginRequest("trainer@link.fit", "password");
        Trainer spyTrainer = spy(trainer);
        doReturn(1L).when(spyTrainer).getId();

        when(trainerRepository.findByEmail(any())).thenReturn(Optional.of(spyTrainer));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtUtil.generateToken(any(Role.class),any(Long.class),any(String.class))).thenReturn("token");

        //when
        TokenResponse response = trainerService.login(request);

        //then
        assertNotNull(response);
        assertEquals("token",response.token());
    }

    @Test
    @DisplayName("login() 테스트 : 존재하지 않는 Email")
    void loginFailed_email() {
        //given
        LoginRequest request = new LoginRequest("trainer@link.fit", "password");

        when(trainerRepository.findByEmail(any())).thenReturn(Optional.empty());

        //when & then
        assertThrows(NotFoundException.class,()->trainerService.login(request));
    }

    @Test
    @DisplayName("login() 테스트 : 일치하지 않는 Password")
    void loginFailed_pw() {
        //given
        LoginRequest request = new LoginRequest("trainer@link.fit", "password");

        when(trainerRepository.findByEmail(any())).thenReturn(Optional.of(trainer));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        //when & then
        assertThrows(PermissionException.class,()->trainerService.login(request));
    }

    @Test
    void getTrainer() {
        //given
        Trainer spyTrainer = spy(trainer);
        doReturn(1L).when(spyTrainer).getId();
        when(trainerRepository.findById(spyTrainer.getId())).thenReturn(Optional.of(spyTrainer));


        //when
        Trainer expectedTrainer = trainerService.getTrainer(spyTrainer.getId());

        //then
        assertEquals(expectedTrainer,spyTrainer);
    }

    @Test
    void getProfile() {
        //given
        Trainer spyTrainer = spy(trainer);
        doReturn(1L).when(spyTrainer).getId();
        when(trainerRepository.findById(spyTrainer.getId())).thenReturn(Optional.of(spyTrainer));

        //when
        TrainerProfileResponse response = trainerService.getProfile(spyTrainer.getId());

        //then
        assertNotNull(response);
        assertEquals(spyTrainer.getName(),response.name());
        assertEquals(spyTrainer.getGender(),response.gender());
    }
}