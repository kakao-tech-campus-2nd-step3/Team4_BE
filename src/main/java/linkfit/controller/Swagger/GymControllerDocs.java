package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import linkfit.annotation.LoginTrainer;
import linkfit.dto.GymDescriptionRequest;
import linkfit.dto.GymDetailResponse;
import linkfit.dto.GymLocationResponse;
import linkfit.dto.GymRegisterRequest;
import linkfit.dto.GymSearchRequest;
import linkfit.dto.GymSearchResponse;
import linkfit.dto.GymTrainersResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Gym(헬스장) API", description = "Gym 관련 API입니다.")
public interface GymControllerDocs {

    @Operation(summary = "헬스장 상세 정보 반환", description = "Gym ID에 해당하는 헬스장의 상세정보 반환")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "상세정보 반환 성공"),
        @ApiResponse(responseCode = "404", description = "Id에 해당하는 헬스장 없음")})
    ResponseEntity<GymDetailResponse> getGymDetails(@PathVariable Long gymId);

    @Operation(summary = "키워드로 헬스장 조회", description = "키워드가 포함되는 헬스장 조회(트레이너 회원 가입시 소속 입력란에서 사용)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "헬스장 조회 성공")})
    ResponseEntity<GymSearchResponse> searchGymByKeyword(
        @RequestBody GymSearchRequest request, @ParameterObject Pageable pageable);

    @Operation(summary = "헬스장 소속 트레이너 조회", description = "Gym Id에 해당하는 헬스장의 소속 트레이너 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "헬스장 소속 트레이너 조회 성공"),
        @ApiResponse(responseCode = "404", description = "Id에 해당하는 헬스장 없음")})
    ResponseEntity<GymTrainersResponse> getGymTrainers(@PathVariable Long gymId,
        @ParameterObject Pageable pageable);

    @Operation(summary = "헬스장 위치 조회", description = "지도API에서 사용할 헬스장 핀포인트 위치 반환")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
        @ApiResponse(responseCode = "404", description = "Id에 해당하는 헬스장 없음")})
    ResponseEntity<List<GymLocationResponse>> getGymLocations();

    @Operation(summary = "헬스장 등록 요청", description = "헬스장이 등록되어있지 않은 경우 관리자에게 헬스장 등록 요청", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "헬스장 등록 요청 성공"),
        @ApiResponse(responseCode = "401", description = "권한 확인 필요")})
    ResponseEntity<Void> sendGymRegistrationRequest(
        @Parameter(hidden = true) @LoginTrainer Long trainerId,
        @Valid @RequestBody GymRegisterRequest gymRegisterRequest);

    @Operation(summary = "헬스장 정보 업데이트", description = "헬스장 ADMIN 권한을 가진 트레이너가 헬스장 정보 업데이트", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "헬스장 정보 업데이트 성공"),
        @ApiResponse(responseCode = "401", description = "권한 확인 필요")})
    ResponseEntity<Void> updateGymInfo(@PathVariable Long gymId,
        @Parameter(hidden = true) @LoginTrainer Long trainerId,
        @RequestPart("description") GymDescriptionRequest gymDescriptionRequest,
        @RequestPart(value = "images", required = false) List<MultipartFile> gymImages);


}
