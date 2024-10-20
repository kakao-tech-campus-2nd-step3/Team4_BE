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
import linkfit.dto.TrainerProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Trainer API", description = "Trainer관련 API입니다.")
public interface TrainerControllerDocs {

    // Trainer 관련 메서드들

    @Operation(summary = "트레이너 프로필 조회(일반회원)", description = "트레이너 ID로 트레이너의 프로필 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "트레이너 프로필 조회 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 트레이너")})
    ResponseEntity<TrainerProfileResponse> getTrainerProfile(
        @Parameter @PathVariable Long trainerId);

    @Operation(summary = "트레이너 본인 프로필 조회", description = "트레이너 본인의 프로필 조회.", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "트레이너 경력 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<TrainerProfileResponse> getMyProfile(
        @Parameter(hidden = true) Token token);
}
