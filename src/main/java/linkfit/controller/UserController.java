package linkfit.controller;

import java.util.List;
import linkfit.annotation.LoginUser;
import linkfit.dto.BodyInfoResponse;
import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;
import linkfit.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@LoginUser Long userId) {
        UserProfileResponse response = userService.getProfile(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(@LoginUser Long userId,
        @RequestPart("user") UserProfileRequest request,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        userService.updateProfile(userId, request, profileImage);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/bodyInfo")
    public ResponseEntity<Void> registerBodyInfo(@LoginUser Long userId,
        @RequestPart(value = "inbodyImage") MultipartFile inbodyImage) {
        userService.registerBodyInfo(userId, inbodyImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/bodyInfo")
    public ResponseEntity<List<BodyInfoResponse>> getAllBodyInfo(@LoginUser Long userId,
        Pageable pageable) {
        List<BodyInfoResponse> responseBody = userService.getAllBodyInfo(userId,
            pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @DeleteMapping("/bodyInfo/{bodyInfoId}")
    public ResponseEntity<Void> deleteBodyInfo(@LoginUser Long userId,
        @PathVariable Long bodyInfoId) {
        userService.deleteBodyInfo(userId, bodyInfoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}