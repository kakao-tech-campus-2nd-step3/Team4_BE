package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import linkfit.dto.SportsResponse;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(name = "Sports(운동 종류) API", description = "운동 종류 관련 API입니다.")
public interface SportsControllerDocs {

    @Operation(summary = "운동 목록 조회", description = "등록된 운동 종류의 목록을 모두 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "운동 목록 조회 성공")})
    ResponseEntity<List<SportsResponse>> getAllSports(@ParameterObject Pageable pageable);
}
