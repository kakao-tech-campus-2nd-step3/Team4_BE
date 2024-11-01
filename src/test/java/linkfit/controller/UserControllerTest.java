package linkfit.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import linkfit.dto.Token;
import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;
import linkfit.service.UserService;
import linkfit.status.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private Gson gson;

    private Token testToken;

    private static final String BASE_URL = "/api/users";

    @BeforeEach
    void setUp() {
        testToken = new Token(Role.USER,1L);
    }

    @Test
    @DisplayName("일반유저 프로필 조회")
    void getUserProfile() throws Exception {
        //given
        UserProfileResponse response = new UserProfileResponse("이름","location","defaultImageUrl");
        when(userService.getProfile(testToken.id())).thenReturn(response);

        //when & then
        mockMvc.perform(get(BASE_URL+"/profile")
            .header("Authorization","Bearer mastertoken-user"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("이름"))
            .andExpect(jsonPath("$.location").value("location"))
            .andExpect(jsonPath("$.profileImageUrl").value("defaultImageUrl"));
    }

    @Test
    @DisplayName("일반유저 프로필 수정")
    void updateProfileTest() throws Exception {
        // given
        UserProfileRequest request = new UserProfileRequest("newName", "newLocation");
        MockMultipartFile userFile = new MockMultipartFile("user", "", "application/json", gson.toJson(request).getBytes());
        MockMultipartFile profileImageFile = new MockMultipartFile("profileImage", "image.png", "image/png", "test image content".getBytes());

        doNothing().when(userService).updateProfile(testToken.id(), request, profileImageFile);

        // when & then
        mockMvc.perform(multipart(BASE_URL + "/profile")
                .file(userFile)
                .file(profileImageFile)
                .header("Authorization", "Bearer mastertoken-user")
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .with(req -> { req.setMethod("PUT"); return req; })) // PUT 메서드로 설정
            .andExpect(status().isNoContent());
    }




}