package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.dto.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Career API", description = "Career(트레이너 - 경력) API입니다.")

public interface CareerControllerDocs {

    // Career 관련 메서드들
    @Operation(summary = "트레이너 경력 조회", description = "로그인한 트레이너의 자신의 경력 조회.", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "트레이너 경력 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    default ResponseEntity<List<CareerResponse>> getCareer(
        @Parameter(hidden = true) Token token) {
        return null;
    }

    @Operation(summary = "트레이너 경력 등록", description = "로그인한 트레이너의 자신의 경력 등록.", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "트레이너 경력 등록 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> addCareer(
        @Parameter(hidden = true) Token token,
        @RequestBody List<CareerRequest> request);

    @Operation(summary = "트레이너 경력 삭제", description = "로그인한 트레이너의 자신의 경력 삭제.", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "트레이너 경력 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> deleteCareer(
        @Parameter(hidden = true) Token token,
        @PathVariable Long careerId);

    @Operation(summary = "트레이너 경력 조회(일반회원)", description = "일반회원이 다른 트레이너의 경력 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "트레이너 경력 조회 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 트레이너")})
    ResponseEntity<List<CareerResponse>> getAllCareerByTrainer(
        @Parameter @PathVariable Long trainerId);
}
