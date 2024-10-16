package linkfit.controller;

import jakarta.validation.Valid;
import java.util.List;
import linkfit.annotation.Login;
import linkfit.controller.Swagger.PtControllerDocs;
import linkfit.dto.ProgressPtDetailResponse;
import linkfit.dto.ProgressPtListResponse;
import linkfit.dto.PtSuggestionRequest;
import linkfit.dto.ReceivePtSuggestResponse;
import linkfit.dto.SendPtSuggestResponse;
import linkfit.dto.Token;
import linkfit.dto.UserPtResponse;
import linkfit.service.PtService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pt")
public class PtController implements PtControllerDocs {

    private final PtService ptService;

    public PtController(PtService ptService) {
        this.ptService = ptService;
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<ProgressPtListResponse>> getTrainerProgressPt(
        @Login Token token, Pageable pageable) {
        List<ProgressPtListResponse> responseBody = ptService.getTrainerProgressPt(token.id(),
            pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/suggests/trainer")
    public ResponseEntity<List<SendPtSuggestResponse>> getAllSendSuggestion(
        @Login Token token, Pageable pageable) {
        List<SendPtSuggestResponse> responseBody = ptService.getAllSendSuggestion(token.id(),
            pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/suggests/user")
    public ResponseEntity<List<ReceivePtSuggestResponse>> getAllReceiveSuggestion(
        @Login Token token, Pageable pageable) {
        List<ReceivePtSuggestResponse> responseBody = ptService.getAllReceiveSuggestion(
            token.id(), pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/user")
    public ResponseEntity<UserPtResponse> getMyPt(@Login Token token) {
        UserPtResponse responseBody = ptService.getMyPt(token.id());
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @PostMapping
    public ResponseEntity<Void> sendSuggestion(@Login Token token,
        @Valid @RequestBody PtSuggestionRequest ptSuggestionRequest) {
        ptService.sendSuggestion(token.id(), ptSuggestionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{ptId}")
    public ResponseEntity<Void> approvalSuggestion(@Login Token token,
        @PathVariable Long ptId) {
        ptService.approvalSuggestion(token.id(), ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{ptId}/trainer")
    public ResponseEntity<Void> recallSuggestion(@Login Token token,
        @PathVariable Long ptId) {
        ptService.recallSuggestion(token.id(), ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{ptId}/user")
    public ResponseEntity<Void> refuseSuggestion(@Login Token token, @PathVariable Long ptId) {
        ptService.refuseSuggestion(token.id(), ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{ptId}/trainer")
    public ResponseEntity<ProgressPtDetailResponse> getProgressUserDetails(
        @Login Token token, @PathVariable Long ptId) {
        ProgressPtDetailResponse responseBody = ptService.getProgressUserDetails(token.id(),
            ptId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }
}