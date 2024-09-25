package linkfit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import linkfit.dto.LoginRequest;
import linkfit.dto.TrainerRegisterRequest;
import linkfit.dto.UserRegisterRequest;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService<User> userAuthService;
    private final AuthService<Trainer> trainerAuthService;

    public AuthController(AuthService<User> userAuthService,
                          AuthService<Trainer> trainerAuthService) {
        this.userAuthService = userAuthService;
        this.trainerAuthService = trainerAuthService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<Void> registerUser(
            @RequestPart("user") UserRegisterRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        userAuthService.register(request, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest request) {
        String token = userAuthService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }

    @PostMapping("/trainer/register")
    public ResponseEntity<Void> registerTrainer(
            @RequestPart("trainer") TrainerRegisterRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        trainerAuthService.register(request, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/trainer/login")
    public ResponseEntity<String> loginTrainer(@RequestBody LoginRequest request) {
        String token = trainerAuthService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
