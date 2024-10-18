package linkfit.controller.Swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import linkfit.annotation.Login;
import linkfit.dto.ChattingRoomResponse;
import linkfit.dto.MessageResponse;
import linkfit.dto.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Chat(채팅) API", description = "채팅 관련 API입니다.")
public interface ChattingControllerDocs {

    @Operation(summary = "자신이 속한 채팅방 조회", description = "유저/트레이너의 토큰을 통해 자신이 속해있는 채팅방의 목록을 가져옵니다.", parameters = {
        @Parameter(name = "Authorization", in = ParameterIn.HEADER, description = "Bearer 토큰 형식의 인증 토큰", required = true)})
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공"),
        @ApiResponse(responseCode = "401", description = "인증 필요")})
    ResponseEntity<List<ChattingRoomResponse>> getMyChatRooms(@Parameter(hidden = true) Token token);

    @Operation(summary = "채팅방의 기존 메시지 목록 조회", description = "채팅방 Id에 해당하는 채팅방의 기존 채팅목록을 가져옵니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 채팅방")})
    ResponseEntity<List<MessageResponse>> getAllMessages(@PathVariable Long roomId);


}
