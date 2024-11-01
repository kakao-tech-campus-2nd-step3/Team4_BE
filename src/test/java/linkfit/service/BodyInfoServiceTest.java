package linkfit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import linkfit.dto.BodyInfoResponse;
import linkfit.entity.BodyInfo;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.BodyInfoRepository;
import linkfit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class BodyInfoServiceTest {

    @Mock
    UserService userService;

    @Mock
    ImageUploadService imageUploadService;

    @Mock
    BodyInfoRepository bodyInfoRepository;

    @InjectMocks
    BodyInfoService bodyInfoService;

    User user;
    MultipartFile multipartFile;
    BodyInfo bodyInfo;
    @BeforeEach
    void setUp() {
        user = new User("user@link.fit","일반회원","defaultProfileImageUrl","강원도 춘천시 백령로 123");
        multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());
        bodyInfo = new BodyInfo(user,"inbodyImageUrl");
    }

    @Test
    @DisplayName("BodyInfo 등록 Test : 성공")
    void registerBodyInfoTest() {
        //given
        when(userService.getUser(1L)).thenReturn(user);
        when(imageUploadService.saveImage(any())).thenReturn("inbodyImageUrl");


        //when
        bodyInfoService.registerBodyInfo(1L,multipartFile);

        //then
        verify(bodyInfoRepository).save(any(BodyInfo.class));
    }

    @Test
    @DisplayName("모든 BodyInfo 조회 Test : 성공")
    void getAllBodyInfo() {
        //given
        when(userService.getUser(any())).thenReturn(user);
        when(bodyInfoRepository.findAllByUser(user,PageRequest.of(0,10)))
            .thenReturn(new PageImpl<>(List.of(bodyInfo)));
        BodyInfoResponse expected = bodyInfo.toDto();
        //when
        List<BodyInfoResponse> response =  bodyInfoService.getAllBodyInfo(1L, PageRequest.of(0,10));

        //then
        assertEquals(response.size(),1);
        assertEquals(response.getFirst(),expected);
    }

    @Test
    @DisplayName("Get BodyInfo Test")
    void getBodyInfo() {
        //given
        when(bodyInfoRepository.findById(any())).thenReturn(Optional.ofNullable(bodyInfo));

        //then
        BodyInfo expected = bodyInfoService.getBodyInfo(user.getId());

        //then
        assertEquals(bodyInfo,expected);
    }

    @Test
    @DisplayName("Get BodyInfo Failed Test: 존재하지 않는 BodyInfo")
    void getBodyInfoFailed() {
        //given
        when(bodyInfoRepository.findById(any())).thenReturn(Optional.empty());

        //when & then
        assertThrows(NotFoundException.class,()->bodyInfoService.getBodyInfo(user.getId()));
    }

    @Test
    @DisplayName("Delete BodyInfo Test: 성공")
    void deleteBodyInfo() {
        //given
        User spyUser = spy(user);
        BodyInfo spyBodyInfo = spy(bodyInfo);
        doReturn(1L).when(spyUser).getId();
        doReturn(spyUser).when(spyBodyInfo).getUser();

        when(bodyInfoRepository.findById(any())).thenReturn(Optional.ofNullable(spyBodyInfo));

        //when
        bodyInfoService.deleteBodyInfo(1L,1L);
        //then
        verify(bodyInfoRepository).delete(any());
    }

    @Test
    @DisplayName("Delete BodyInfo Failed Test: Permission Denied")
    void deleteBodyInfoFailed() {
        //given
        User spyUser = spy(user);
        BodyInfo spyBodyInfo = spy(bodyInfo);
        doReturn(2L).when(spyUser).getId();
        doReturn(spyUser).when(spyBodyInfo).getUser();

        when(bodyInfoRepository.findById(any())).thenReturn(Optional.ofNullable(spyBodyInfo));

        //when & then
        assertThrows(PermissionException.class,()->bodyInfoService.deleteBodyInfo(1L,1L));

    }
}
