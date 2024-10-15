package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import linkfit.annotation.Login;
import linkfit.dto.Token;
import linkfit.dto.UserProfileRequest;
import linkfit.dto.UserProfileResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User(일반회원)", description = "User 관련 API입니다.")
public interface UserControllerDocs {

    @Operation(summary = "유저 프로필 조회", description = "로그인한 유저의 자신의 프로필 조회.", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "유저 프로필 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<UserProfileResponse> getProfile(
        @Parameter(hidden = true) @Login Token token);

    @Operation(summary = "유저 프로필 수정", description = "로그인한 유저의 자신의 프로필 수정.", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "유저 프로필 수정 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> updateProfile(
        @Parameter(hidden = true) @Login Token token,
        @RequestPart("user") UserProfileRequest request,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage);
}