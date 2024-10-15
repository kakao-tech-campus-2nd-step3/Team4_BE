package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import linkfit.annotation.Login;
import linkfit.dto.ReviewRequest;
import linkfit.dto.ReviewResponse;
import linkfit.dto.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Review(리뷰) API", description = "Review 관련 API입니다.")
public interface ReviewControllerDocs {

    @Operation(summary = "트레이너의 리뷰 조회", description = "Trianer Id에 해당하는 트레이너의 리뷰 조회")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
        @ApiResponse(responseCode = "404", description = "Id에 해당하는 트레이너 없음")})
    ResponseEntity<List<ReviewResponse>> getTrainerReviews(
        @PathVariable("trainerId") Long trainerId);

    @Operation(summary = "일반회원) 작성한 리뷰 조회", description = "일반회원의 자신이 작성한 모든 리뷰 조회", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
        @ApiResponse(responseCode = "401", description = "권한 확인 필요")})
    ResponseEntity<List<ReviewResponse>> getMyReviewByUser(
        @Parameter(hidden = true) @Login Token token);

    @Operation(summary = "트레이너) 작성한 리뷰 조회", description = "트레이너의 자신이 받은 모든 리뷰 조회", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
        @ApiResponse(responseCode = "401", description = "권한 확인 필요")})
    ResponseEntity<List<ReviewResponse>> getMyReviewByTrainer(
        @Parameter(hidden = true) Token token);

    @Operation(summary = "리뷰 작성", description = "회원이 Trainer Id에 해당하는 트레이너에게 완료된 PT에 대해 리뷰 작성", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "리뷰 작성 성공"),
        @ApiResponse(responseCode = "401", description = "권한 확인 필요")})
    ResponseEntity<Void> addReview(@Parameter(hidden = true) @Login Token token,
        @RequestBody
        ReviewRequest request, @PathVariable("trainerId") Long trainerId);

    @Operation(summary = "리뷰 삭제", description = "회원이 Review Id에 해당하는 리뷰(본인이 작성한 리뷰) 삭제", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "리뷰 삭제 성공"),
        @ApiResponse(responseCode = "401", description = "권한 확인 필요")})
    ResponseEntity<Void> deleteReviewById(
        @Parameter(hidden = true) Token token,
        @PathVariable("reviewId") Long reviewId);


}
