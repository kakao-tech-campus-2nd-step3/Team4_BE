package linkfit.controller;

import jakarta.validation.Valid;
import java.util.List;
import linkfit.dto.PtSuggestionRequest;
import linkfit.dto.ReceivePtSuggestResponse;
import linkfit.dto.SendPtSuggestResponse;
import linkfit.dto.ProgressPtDetailResponse;
import linkfit.dto.ProgressPtListResponse;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pt")
public class PtController {

    private final PtService ptService;

    public PtController(PtService ptService) {
        this.ptService = ptService;
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<ProgressPtListResponse>> getTrainerProgressPt(
        @RequestHeader("Authorization") String authorization, Pageable pageable) {
        List<ProgressPtListResponse> responseBody = ptService.getTrainerProgressPt(authorization, pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/suggests/trainer")
    public ResponseEntity<List<SendPtSuggestResponse>> getAllSendSuggestion(
        @RequestHeader("Authorization") String authorization, Pageable pageable) {
        List<SendPtSuggestResponse> responseBody = ptService.getAllSendSuggestion(authorization,
            pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/suggests/user")
    public ResponseEntity<List<ReceivePtSuggestResponse>> getAllReceiveSuggestion(
        @RequestHeader("Authorization") String authorization, Pageable pageable) {
        List<ReceivePtSuggestResponse> responseBody = ptService.getAllReceiveSuggestion(
            authorization, pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/user")
    public ResponseEntity<UserPtResponse> getMyPt(
        @RequestHeader("Authorization") String authorization) {
        UserPtResponse responseBody = ptService.getMyPt(authorization);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @PostMapping
    public ResponseEntity<Void> sendSuggestion(@RequestHeader("Authorization") String authorization,
        @Valid @RequestBody PtSuggestionRequest ptSuggestionRequest) {
        ptService.sendSuggestion(authorization, ptSuggestionRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @PutMapping("/{ptId}")
    public ResponseEntity<Void> approvalSuggestion(
        @RequestHeader("Authorization") String authorization, @PathVariable Long ptId) {
        ptService.approvalSuggestion(authorization, ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/{ptId}/trainer")
    public ResponseEntity<Void> recallSuggestion(
        @RequestHeader("Authorization") String authorization, @PathVariable Long ptId) {
        ptService.recallSuggestion(authorization, ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/{ptId}/user")
    public ResponseEntity<Void> refuseSuggestion(
        @RequestHeader("Authorization") String authorization, @PathVariable Long ptId) {
        ptService.refuseSuggestion(authorization, ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping("/{ptId}/trainer")
    public ResponseEntity<ProgressPtDetailResponse> getProgressUserDetails(
        @RequestHeader("Authorization") String authorization, @PathVariable Long ptId) {
        ProgressPtDetailResponse responseBody = ptService.getProgressUserDetails(authorization, ptId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }
}