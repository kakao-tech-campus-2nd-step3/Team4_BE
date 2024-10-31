package linkfit.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import linkfit.dto.Token;
import linkfit.dto.TrainerProfileResponse;
import linkfit.service.TrainerService;
import linkfit.status.Role;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerService trainerService;

    @Autowired
    private Gson gson;

    private Token testToken;

    private static final String BASE_URL = "/api/trainers";

    @BeforeEach
    void setUp() {
        testToken = new Token(Role.USER,1L);
    }

    @Test
    @DisplayName("ID로 트레이너 프로필 조회")
    void getTrainerProfile() throws Exception{
        //given
        TrainerProfileResponse response = new TrainerProfileResponse("이름", TrainerGender.MALE,"defaultProfileImageUrl","Gym");
        when(trainerService.getProfile(1L)).thenReturn(response);

        //when & then
        mockMvc.perform(get(BASE_URL+"/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("이름"))
            .andExpect(jsonPath("$.gender").value("MALE"))
            .andExpect(jsonPath("$.profileImageUrl").value("defaultProfileImageUrl"))
            .andExpect(jsonPath("$.gymName").value("Gym"));


    }

    @Test
    @DisplayName("토큰으로 자신의 프로필 조회")
    void getMyProfile() throws Exception {
        TrainerProfileResponse response = new TrainerProfileResponse("이름", TrainerGender.MALE,"defaultProfileImageUrl","Gym");
        when(trainerService.getProfile(1L)).thenReturn(response);

        //when & then
        mockMvc.perform(get(BASE_URL+"/profile")
            .header("Authorization","Bearer mastertoken-trainer"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("이름"))
            .andExpect(jsonPath("$.gender").value("MALE"))
            .andExpect(jsonPath("$.profileImageUrl").value("defaultProfileImageUrl"))
            .andExpect(jsonPath("$.gymName").value("Gym"));
    }
}