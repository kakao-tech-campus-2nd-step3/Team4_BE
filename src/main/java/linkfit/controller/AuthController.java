package linkfit.controller;

import linkfit.controller.Swagger.AuthControllerDocs;
import linkfit.dto.LoginRequest;
import linkfit.dto.TokenResponse;
import linkfit.dto.TrainerRegisterRequest;
import linkfit.dto.UserRegisterRequest;
import linkfit.service.TrainerService;
import linkfit.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final TrainerService trainerService;

    public AuthController(UserService userService, TrainerService trainerService) {
        this.userService = userService;
        this.trainerService = trainerService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<Void> registerUser(
        @RequestPart(value ="user") UserRegisterRequest request,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user/login")
    public ResponseEntity<TokenResponse> loginUser(@RequestBody LoginRequest request) {
        TokenResponse token = userService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(token);
    }

    @PostMapping("/trainer/register")
    public ResponseEntity<Void> registerTrainer(
        @RequestPart(value = "trainer") TrainerRegisterRequest request,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        trainerService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/trainer/login")
    public ResponseEntity<TokenResponse> loginTrainer(@RequestBody LoginRequest request) {
        TokenResponse token = trainerService.login(request);
        return ResponseEntity.status(HttpStatus.OK)
            .body(token);
    }
}