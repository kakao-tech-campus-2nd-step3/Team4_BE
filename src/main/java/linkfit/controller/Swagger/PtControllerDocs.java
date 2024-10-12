package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import linkfit.annotation.Login;
import linkfit.dto.ProgressPtDetailResponse;
import linkfit.dto.ProgressPtListResponse;
import linkfit.dto.PtSuggestionRequest;
import linkfit.dto.ReceivePtSuggestResponse;
import linkfit.dto.SendPtSuggestResponse;
import linkfit.dto.UserPtResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "PT API", description = "PT 관련 API입니다.")
public interface PtControllerDocs {

    @Operation(summary = "트레이너) 진행중인 PT 회원 목록", description = "트레이너의 진행중인 PT 회원 목록을 모두 조회", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PT 진행중인 유저 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<List<ProgressPtListResponse>> getTrainerProgressPt(
        @Parameter(hidden = true) Long trainerId, @ParameterObject Pageable pageable);

    @Operation(summary = "트레이너) 보낸 PT 제안 목록 ", description = "트레이너가 유저에게 제안한 PT 목록 조회", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "제안목록 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<List<SendPtSuggestResponse>> getAllSendSuggestion(
        @Parameter(hidden = true) Long trainerId, @ParameterObject Pageable pageable);

    @Operation(summary = "일반회원) 받은 제안 목록", description = "거절한 제안을 제외한 받은 제안 목록 조회", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "받은 제안 목록 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<List<ReceivePtSuggestResponse>> getAllReceiveSuggestion(
        @Parameter(hidden = true) @Login Long userId, @ParameterObject Pageable pageable);

    @Operation(summary = "일반유저) 진행중인 PT 상세 조회", description = "진행중인 PT의 상세정보 조회", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "진행중인 PT상세정보 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<UserPtResponse> getMyPt(
        @Parameter(hidden = true) @Login Long userId);

    @Operation(summary = "트레이너) PT 제안 보내기", description = "트레이너가 매칭가능한 회원에게 PT 제안", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "제안 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> sendSuggestion(@Parameter(hidden = true) Long trainerId,
        @Valid @RequestBody PtSuggestionRequest ptSuggestionRequest);

    @Operation(summary = "일반유저) 제안 수락", description = "일반회원이 트레이너에게 받은 제안을 수락. PT의 상태변경", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PT 매칭 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> approvalSuggestion(
        @Parameter(hidden = true) @Login Long userId, @PathVariable Long ptId);

    @Operation(summary = "트레이너) 보낸 제안 회수", description = "트레이너가 보낸 제안을 회수(취소)", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "보낸 제안 회수 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> recallSuggestion(
        @Parameter(hidden = true) Long trainerId, @PathVariable Long ptId);

    @Operation(summary = "일반유저) 받은 제안 거절", description = "일반 회원이 트레이너에게 받은 제안을 거절 (더이상 목록에 조회되지 않음)", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "유저 프로필 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> refuseSuggestion(
        @Parameter(hidden = true) @Login Long userId, @PathVariable Long ptId);

    @Operation(summary = "트레이너) PT 회원 상세정보 조회", description = "트레이너가 PT ID에 해당하는 PT의 정보와 회원의 상세정보를 조회", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PT, 회원정보 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<ProgressPtDetailResponse> getProgressUserDetails(
        @Parameter(hidden = true) Long trainerId, @PathVariable Long ptId);
}
