package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import linkfit.dto.LoginRequest;
import linkfit.dto.TokenResponse;
import linkfit.dto.TrainerRegisterRequest;
import linkfit.dto.UserRegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Auth(인증) API", description = "Auth 관련 API입니다.")
public interface AuthControllerDocs {

    @Operation(summary = "일반회원 회원가입", description = "일반회원으로 회원가입.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일"),
    })
    ResponseEntity<Void> registerUser(
        @RequestPart("user") UserRegisterRequest request);

    @Operation(summary = "일반회원 로그인", description = "이메일, 비밀번호로 인증 후 토큰 발급.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 회원정보")
    })
    ResponseEntity<TokenResponse> loginUser(@RequestBody LoginRequest request);

    @Operation(summary = "트레이너 회원가입", description = "트레이너로 회원가입.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일")
    })
    ResponseEntity<Void> registerTrainer(
        @RequestPart("trainer") TrainerRegisterRequest request);

    @Operation(summary = "트레이너 로그인", description = "이메일, 비밀번호로 인증 후 토큰 발급.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "회원가입 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 회원정보")
    })
    ResponseEntity<TokenResponse> loginTrainer(@RequestBody LoginRequest request);
}
