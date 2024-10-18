package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import linkfit.annotation.Login;
import linkfit.dto.BodyInfoResponse;
import linkfit.dto.Token;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "BodyInfo(유저 - 신체정보) API", description = "BodyInfo 관련 API입니다.")
public interface BodyInfoControllerDocs {

    @Operation(summary = "유저 신체정보 등록", description = "로그인한 유저의 신체정보 등록.", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "유저 신체정보 등록 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<Void> registerBodyInfo(
        @Parameter(hidden = true) @Login Token token,
        @RequestPart(value = "inbodyImage") MultipartFile inbodyImage);

    @Operation(summary = "유저의 모든 신체정보 불러오기", description = "로그인한 유저의 모든 신체정보 조회", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "유저 신체정보 등록 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<List<BodyInfoResponse>> getAllBodyInfo(
        @Parameter(hidden = true) @Login Token token,
        @ParameterObject Pageable pageable);

    @Operation(summary = "유저의 특정 신체정보 삭제", description = "로그인한 유저의 특정 신체정보 ID로 삭제", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "유저 신체정보 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 신체정보 ID")
    })
    ResponseEntity<Void> deleteBodyInfo(@Parameter(hidden = true) @Login Token token,
        @PathVariable Long bodyInfoId);
}