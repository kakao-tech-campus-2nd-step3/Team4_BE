package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import linkfit.annotation.LoginTrainer;
import linkfit.annotation.LoginUser;
import linkfit.dto.PreferenceRequest;
import linkfit.dto.PreferenceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Preferences(선호) API", description = "운동 선호 관련 API입니다.")
public interface PreferenceControllerDocs {

    @Operation(summary = "선호 등록", description = "선호를 등록하고 매칭받을수 있는 상태로 전환", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "선호 등록 성공"),
        @ApiResponse(responseCode = "401", description = "권한 확인 필요")})
    ResponseEntity<Void> registerPreference(
        @Parameter(hidden = true) @LoginUser Long userId,
        @RequestBody PreferenceRequest request);

    @Operation(summary = "매칭가능 회원 보기", description = "트레이너가 조건에 맞는 선호를 등록한(매칭 가능한) 회원을 조회", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "매칭가능 회원 목록 조회 성공"),
        @ApiResponse(responseCode = "401", description = "권한 확인 필요")})
    ResponseEntity<List<PreferenceResponse>> getAllMatchingPossible(
        @Parameter(hidden = true) @LoginTrainer Long trainerId);

    @Operation(summary = "선호 삭제", description = "등록된 선호를 삭제하고 제안을 받지 않는 상태로 전환", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "선호 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "권한 확인 필요")})
    ResponseEntity<Void> deletePreference(
        @PathVariable Long preferenceId,
        @Parameter(hidden = true) @LoginUser Long userId);

}
