package linkfit.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestHeader;
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
    public ResponseEntity<UserProfileResponse> getProfile(
        @RequestHeader("Authorization") String authorization) {
        UserProfileResponse response = userService.getProfile(authorization);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<Void> updateProfile(
        @RequestHeader("Authorization") String authorization,
        @RequestPart("user") UserProfileRequest request,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        userService.updateProfile(authorization, request, profileImage);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/info")
    public ResponseEntity<Void> registerBodyInfo(
        @RequestHeader("Authorization") String authorization,
        @RequestPart(value = "inbodyImage") MultipartFile inbodyImage) {
        userService.registerBodyInfo(authorization, inbodyImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/info")
    public ResponseEntity<List<BodyInfoResponse>> getAllBodyInfo(
        @RequestHeader("Authorization") String authorization,
        Pageable pageable) {
        List<BodyInfoResponse> responseBody = userService.getAllBodyInfo(authorization,
            pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @DeleteMapping("/info/{infoId}")
    public ResponseEntity<Void> deleteBodyInfo(@RequestHeader("Authorization") String authorization,
        @PathVariable("infoId") Long infoId) {
        userService.deleteBodyInfo(authorization, infoId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}