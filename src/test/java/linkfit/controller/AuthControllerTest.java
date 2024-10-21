package linkfit.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import linkfit.dto.LoginRequest;
import linkfit.dto.TokenResponse;
import linkfit.dto.TrainerRegisterRequest;
import linkfit.dto.UserRegisterRequest;
import linkfit.service.TrainerService;
import linkfit.service.UserService;
import linkfit.status.TrainerGender;
import linkfit.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TrainerService trainerService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private Gson gson;

    private static final String BASE_URL = "/api/auth";


    @Test
    @DisplayName("일반회원 회원가입 테스트 - 성공")
    void registerUser() throws Exception {
        //given
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest(
            "user@link.fit", "password",
            "김근육", "강원특별자치도 춘천시 백령로 1492번길");
        doNothing().when(userService).register(any(UserRegisterRequest.class));

        //when
        MockMultipartFile content = new MockMultipartFile(
            "user", "", MediaType.APPLICATION_JSON_VALUE,
            gson.toJson(userRegisterRequest).getBytes()
        );

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.multipart(BASE_URL + "/user/register")
                    .file(content)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("트레이너 회원가입 테스트 - 성공")
    void registerTrainer() throws Exception {
        //given
        TrainerRegisterRequest trainerRegisterRequest = new TrainerRegisterRequest(
            "trainer@link.fit", "password",
            "김근육", TrainerGender.MALE);

        doNothing().when(trainerService).register(any(TrainerRegisterRequest.class));

        //when
        MockMultipartFile content = new MockMultipartFile(
            "trainer", "", MediaType.APPLICATION_JSON_VALUE,
            gson.toJson(trainerRegisterRequest).getBytes()
        );

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.multipart(BASE_URL + "/trainer/register")
                    .file(content)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated());


    }

    @Test
    @DisplayName("트레이너 회원가입 테스트 - 실패")
    void registerInvalidTrainer() throws Exception {
        //given
        TrainerRegisterRequest invalidEmailRequest = new TrainerRegisterRequest(
            "trainer", "password",
            "김근육", TrainerGender.MALE);

        TrainerRegisterRequest invalidNameRequest = new TrainerRegisterRequest(
            "trainer", "password",
            "", TrainerGender.MALE);

        TrainerRegisterRequest invalidPasswordRequest = new TrainerRegisterRequest("trainer",
            "",
            "김근육", TrainerGender.MALE);

        doNothing().when(trainerService).register(any(TrainerRegisterRequest.class));

        //when
        MockMultipartFile invalidEmailcontent = new MockMultipartFile(
            "trainer", "", MediaType.APPLICATION_JSON_VALUE,
            gson.toJson(invalidEmailRequest).getBytes()
        );

        MockMultipartFile invalidNameContent = new MockMultipartFile(
            "trainer", "", MediaType.APPLICATION_JSON_VALUE,
            gson.toJson(invalidNameRequest).getBytes()
        );

        MockMultipartFile invalidPasswordContent = new MockMultipartFile(
            "trainer", "", MediaType.APPLICATION_JSON_VALUE,
            gson.toJson(invalidPasswordRequest).getBytes()
        );


        //then
        mockMvc.perform(
                MockMvcRequestBuilders.multipart(BASE_URL + "/trainer/register")
                    .file(invalidEmailcontent)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("이메일 형식이 잘못되었습니다.")));

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(BASE_URL + "/trainer/register")
                    .file(invalidNameContent)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("이름은 필수입니다.")));

        mockMvc.perform(
                MockMvcRequestBuilders.multipart(BASE_URL + "/trainer/register")
                    .file(invalidPasswordContent)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(containsString("비밀번호는 필수입니다.")));


    }


    @Test
    @DisplayName("유저 로그인 - 성공")
    void loginUser() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest(
            "user@link.fit", "password");
        TokenResponse tokenResponse = new TokenResponse("token");

        when(userService.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        //when & then
        mockMvc.perform(post(BASE_URL+"/user/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    @DisplayName("트레이너 로그인 - 성공")
    void loginTrainer() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest(
            "trainer@link.fit", "password");
        TokenResponse tokenResponse = new TokenResponse("token");

        when(trainerService.login(any(LoginRequest.class))).thenReturn(tokenResponse);

        //when & then
        mockMvc.perform(post(BASE_URL+"/trainer/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("token"));
    }
}