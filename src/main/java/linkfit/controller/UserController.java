package linkfit.controller;

import linkfit.annotation.Login;
import linkfit.controller.Swagger.UserControllerDocs;
import linkfit.dto.Token;
import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;
import linkfit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController implements UserControllerDocs {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@Login Token token) {
        UserProfileResponse response = userService.getProfile(token.id());
        return ResponseEntity.status(HttpStatus.OK)
            .body(response);
    }

    @PutMapping(value = "/profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> updateProfile(
        @Login Token token,
        @RequestPart("user") UserProfileRequest request,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        userService.updateProfile(token.id(), request, profileImage);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}