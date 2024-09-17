package linkfit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import linkfit.dto.UserRegisterRequest;
import linkfit.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/user/register")
	public ResponseEntity<Void> userRegister(
			@RequestPart("user") UserRegisterRequest request,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage){
		authService.userRegister(request, profileImage);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
}
