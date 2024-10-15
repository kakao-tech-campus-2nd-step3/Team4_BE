package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import linkfit.annotation.Login;
import linkfit.dto.ScheduleRequest;
import linkfit.dto.ScheduleResponse;
import linkfit.dto.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Schedule(PT일정) API", description = "Schedule 관련 API입니다.")
public interface ScheduleControllerDocs {

    @Operation(summary = "PT 일정 조회", description = "PT ID에 해당하는 PT의 일정 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "PT 일정 조회 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 PT")})
    ResponseEntity<ScheduleResponse> getSchedules(@PathVariable Long ptId);

    @Operation(summary = "스케줄 추가", description = "트레이너가 진행중인 PT에 일정 추가", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "일정 추가 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> registerSchedule(
        @Parameter(hidden = true) Token Token,
        @PathVariable Long ptId, @Valid @RequestBody ScheduleRequest scheduleRequest);

    @Operation(summary = "스케줄 완료", description = "유저가 운동을 진행한 후 Schedule 완료처리", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "스케줄 완료 처리 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> completeSchedule(
        @Parameter(hidden = true) @Login Token token,
        @PathVariable Long ptId, @PathVariable Long scheduleId);

    @Operation(summary = "스케줄 삭제", description = "트레이너가 PT 스케줄 삭제", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "스케줄 삭제 완료"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> deleteSchedule(@Parameter(hidden = true) Token token,
        @PathVariable Long ptId, @PathVariable Long scheduleId);
}
