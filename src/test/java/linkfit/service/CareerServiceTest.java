package linkfit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.entity.Career;
import linkfit.entity.Trainer;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.CareerRepository;
import linkfit.repository.TrainerRepository;
import linkfit.status.TrainerGender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CareerServiceTest {

    @Mock
    CareerRepository careerRepository;

    @Mock
    TrainerRepository trainerRepository;

    @InjectMocks
    CareerService careerService;

    Career career1 ;
    Career career2 ;
    Trainer trainer;
    @BeforeEach
    void setUp() {
        trainer = new Trainer("trainer@link.fit", "password", "트레이너1", TrainerGender.MALE);
        career1 = new Career(trainer,"경력 1");
        career2 = new Career(trainer,"경력 2");

    }

    @Test
    @DisplayName("TrainerID로 Career 모두 조회: 성공")
    void getAllCareers() {
        //given

        when(trainerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(trainer));
        when(careerRepository.findAllByTrainer(trainer)).thenReturn(List.of(career1,career2));

        //when
        List<CareerResponse> response = careerService.getAllCareers(1L);

        //then
        assertEquals(2, response.size());
        assertEquals(career1.toDto(),response.getFirst());

    }

    @Test
    @DisplayName("TrainerID로 Career 모두 조회: 존재하지 않는 Trainer")
    void getAllCareersFailed() {
        //given
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        assertThrows(NotFoundException.class, () -> careerService.getAllCareers(1L));
    }

    @Test
    @DisplayName("Career 저장 테스트: 성공")
    void addCareer() {
        //given
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(trainer));
        List<CareerRequest> requests = new ArrayList<>();
        requests.add(new CareerRequest("경력 1"));
        requests.add(new CareerRequest("경력 2"));

        //when
        careerService.addCareer(1L,requests);

        //then
        verify(careerRepository,times(2)).save(any(Career.class));
    }

    @Test
    @DisplayName("Id로 커리어 삭제: 성공")
    void deleteCareer() {
        //given
        Career spyCareer = spy(career1);
        Trainer spyTrainer = spy(trainer);
        doReturn(spyTrainer).when(spyCareer).getTrainer();
        doReturn(1L).when(spyTrainer).getId();
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(spyTrainer));
        when(careerRepository.findById(anyLong())).thenReturn(Optional.of(spyCareer));



        //when
        careerService.deleteCareer(1L,1L);

        //then
        verify(careerRepository,times(1)).deleteById(any(Long.class));


    }

    @Test
    @DisplayName("Id로 커리어 삭제: 실패 ")
    void deleteCareerFailed() {
        //given
        Career spyCareer = spy(career1);
        Trainer spyTrainer = spy(trainer);
        doReturn(spyTrainer).when(spyCareer).getTrainer();
        doReturn(2L).when(spyTrainer).getId();
        when(trainerRepository.findById(anyLong())).thenReturn(Optional.of(spyTrainer));
        when(careerRepository.findById(anyLong())).thenReturn(Optional.of(spyCareer));



        //when & then
        assertThrows(PermissionException.class, () -> careerService.deleteCareer(1L,1L));

    }
}