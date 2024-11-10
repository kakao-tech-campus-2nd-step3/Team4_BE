package linkfit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import linkfit.dto.SportsRequest;
import linkfit.dto.SportsResponse;
import linkfit.entity.Sports;
import linkfit.exception.DuplicateException;
import linkfit.exception.NotFoundException;
import linkfit.repository.SportsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SportsServiceTest {

    @Mock
    SportsRepository sportsRepository;

    @InjectMocks
    SportsService sportsService;



    @Test
    void registerSport() {
        //given
        SportsRequest request = new SportsRequest("sports");

        //when
        sportsService.registerSport(request);

        //then
        verify(sportsRepository,times(1)).save(any(Sports.class));
    }

    @Test
    void getAllSports() {
        //given
        Sports sports1 = new Sports("sports1");
        Sports sports2 = new Sports("sports2");

        when(sportsRepository.findAll(PageRequest.of(0,10))).thenReturn(new PageImpl<>(List.of(sports1,sports2)));

        //when
        List<SportsResponse> responses = sportsService.getAllSports(PageRequest.of(0, 10));

        //then
        assertEquals(responses.size(), 2);
        assertEquals(responses.get(0).name(), sports1.getName());
        assertEquals(responses.get(1).name(), sports2.getName());

    }

    @Test
    void findSportsById() {
        //given
        Sports sports = new Sports("sports1");
        when(sportsRepository.findById(any())).thenReturn(Optional.of(sports));

        //when
        Sports expected = sportsService.findSportsById(1L);

        //then
        verify(sportsRepository,times(1)).findById(any());
        assertEquals(expected, sports);
    }

    @Test
    @DisplayName("Sports rename Test: 성공")
    void renameSports() {
        //given
        Sports sports = new Sports("sports1");
        when(sportsRepository.findById(any())).thenReturn(Optional.of(sports));
        SportsRequest request = new SportsRequest("changed Name");

        //when
        sportsService.renameSports(1L, request);

        //then
        verify(sportsRepository,times(1)).save(any(Sports.class));
        assertEquals(sports.getName(), "changed Name");
    }

    @Test
    @DisplayName("Sports rename Test: 중복된 이름")
    void renameSportsFailed() {
        //given
        Sports sports = new Sports("sports1");
        SportsRequest request = new SportsRequest("sports1");

        when(sportsRepository.findById(any())).thenReturn(Optional.of(sports));
        when(sportsRepository.existsByName(any())).thenReturn(true);

        //when & then
        assertThrows(DuplicateException.class,()->sportsService.renameSports(1L, request));
    }

    @Test
    void deleteSports() {
        when(sportsRepository.existsById(any())).thenReturn(true);

        //when
        sportsService.deleteSports(1L);

        //then
        verify(sportsRepository,times(1)).deleteById(any());


    }

    @Test
    @DisplayName("Sports Id로 조회 : 실패")
    void getSportsById() {
        //given
        when(sportsRepository.findById(any())).thenReturn(Optional.empty());

        //when & then
        assertThrows(NotFoundException.class, ()->sportsService.getSportsById(1L));
    }
}