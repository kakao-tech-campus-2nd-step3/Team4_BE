package linkfit.controller;

import jakarta.validation.Valid;
import java.util.List;
import linkfit.dto.UserBodyInfoRequest;
import linkfit.dto.UserBodyInfoResponse;
import linkfit.dto.UserRequest;
import linkfit.dto.UserResponse;
import linkfit.entity.UserBodyInfo;
import linkfit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(
        @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.getProfile(authorization));
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(
        @RequestHeader("Authorization") String authorization,
        @Valid @RequestBody UserRequest userRequest) {
        userService.updateProfile(authorization, userRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/info")
    public ResponseEntity<?> registerBodyInfo(
        @RequestHeader("Authorization") String authorization,
        @Valid @RequestBody UserBodyInfoRequest userBodyInfoRequest) {
        userService.registerBodyInfo(authorization, userBodyInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/info")
    public List<UserBodyInfoResponse> getAllBodyInfo(
        @RequestHeader("Authorization") String authorization,
        Pageable pageable) {
        return pageToList(userService.getAllBodyInfo(authorization, pageable));
    }

    private List<UserBodyInfoResponse> pageToList(Page<UserBodyInfo> bodyInfos) {
        return bodyInfos.stream()
            .map(UserBodyInfoResponse::new)
            .toList();
    }
}