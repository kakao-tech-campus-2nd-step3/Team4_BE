package linkfit.service;

import java.util.Optional;
import linkfit.component.DefaultImageProvider;
import linkfit.dto.*;
import linkfit.entity.User;
import linkfit.exception.DuplicateException;
import linkfit.exception.PermissionException;
import linkfit.repository.UserRepository;
import linkfit.status.Role;
import linkfit.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private ImageUploadService imageUploadService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private DefaultImageProvider defaultImageProvider;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("user@link.fit", "encodedPassword", "name", "location");
    }

    @Test
    @DisplayName("register() 테스트")
    void register() {
        // given
        UserRegisterRequest request = new UserRegisterRequest("user@link.fit", "password", "name",
            "location");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(defaultImageProvider.getDefaultImageUrl()).thenReturn("defaultImageUrl");

        // when
        userService.register(request);

        // then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("register() 중복 이메일 예외 테스트")
    void register_duplicateEmail() {
        // given
        UserRegisterRequest request = new UserRegisterRequest("user@link.fit", "password", "name",
            "location");
        when(userRepository.existsByEmail(any())).thenReturn(true);

        // when & then
        assertThrows(DuplicateException.class, () -> userService.register(request));
    }

    @Test
    @DisplayName("login() 테스트 : 성공")
    void loginSucceed() {
        // given
        LoginRequest request = new LoginRequest("user@link.fit", "password");
        User spyUser = spy(user);
        doReturn(1L).when(spyUser).getId();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(spyUser));
        when(jwtUtil.generateToken(any(Role.class), any(Long.class), any(String.class))).thenReturn(
            "token");
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        // when
        TokenResponse response = userService.login(request);

        // then
        assertNotNull(response);
        assertEquals("token", response.token());
    }

    @Test
    @DisplayName("login() 테스트 : 비밀번호 오류")
    void loginFailed() {
        // given
        LoginRequest request = new LoginRequest("user@link.fit", "password");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        // when & then
        assertThrows(PermissionException.class, () -> userService.login(request));
    }


    @Test
    @DisplayName("getProfile() 테스트")
    void getProfile() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        UserProfileResponse response = userService.getProfile(1L);

        // then
        assertNotNull(response);
        assertEquals("name", response.name());
        assertEquals("location", response.location());

    }

    @Test
    @DisplayName("updateProfile() 테스트")
    void updateProfile() {
        // given
        UserProfileRequest request = new UserProfileRequest("newLocation", "newName");
        MultipartFile profileImage = mock(MultipartFile.class);

        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
        when(imageUploadService.uploadProfileImage(any())).thenReturn("newProfileImageUrl");

        // when
        userService.updateProfile(1L, request, profileImage);

        // then
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("newName", user.getName());
        assertEquals("newLocation", user.getLocation());
        assertEquals("newProfileImageUrl", user.getProfileImageUrl());
    }

    @Test
    @DisplayName("getUser() 테스트")
    void getUser() {
        // given
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // when
        User foundUser = userService.getUser(1L);

        // then
        assertNotNull(foundUser);
        assertEquals("user@link.fit", foundUser.getEmail());
    }
}
