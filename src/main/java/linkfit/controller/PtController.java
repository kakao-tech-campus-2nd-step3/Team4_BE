package linkfit.controller;

import jakarta.validation.Valid;
import java.util.List;
import linkfit.annotation.LoginTrainer;
import linkfit.annotation.LoginUser;
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
        @LoginTrainer Long trainerId, Pageable pageable) {
        List<ProgressPtListResponse> responseBody = ptService.getTrainerProgressPt(trainerId,
            pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/suggests/trainer")
    public ResponseEntity<List<SendPtSuggestResponse>> getAllSendSuggestion(
        @LoginTrainer Long trainerId, Pageable pageable) {
        List<SendPtSuggestResponse> responseBody = ptService.getAllSendSuggestion(trainerId,
            pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/suggests/user")
    public ResponseEntity<List<ReceivePtSuggestResponse>> getAllReceiveSuggestion(
        @LoginUser Long userId, Pageable pageable) {
        List<ReceivePtSuggestResponse> responseBody = ptService.getAllReceiveSuggestion(
            userId, pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/user")
    public ResponseEntity<UserPtResponse> getMyPt(
        @LoginUser Long userId) {
        UserPtResponse responseBody = ptService.getMyPt(userId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @PostMapping
    public ResponseEntity<Void> sendSuggestion(@LoginTrainer Long trainerId,
        @Valid @RequestBody PtSuggestionRequest ptSuggestionRequest) {
        ptService.sendSuggestion(trainerId, ptSuggestionRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @PutMapping("/{ptId}")
    public ResponseEntity<Void> approvalSuggestion(
        @LoginUser Long userId, @PathVariable Long ptId) {
        ptService.approvalSuggestion(userId, ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/{ptId}/trainer")
    public ResponseEntity<Void> recallSuggestion(
        @LoginTrainer Long trainerId, @PathVariable Long ptId) {
        ptService.recallSuggestion(trainerId, ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @DeleteMapping("/{ptId}/user")
    public ResponseEntity<Void> refuseSuggestion(
        @LoginUser Long userId, @PathVariable Long ptId) {
        ptService.refuseSuggestion(userId, ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping("/{ptId}/trainer")
    public ResponseEntity<ProgressPtDetailResponse> getProgressUserDetails(
        @LoginTrainer Long trainerId, @PathVariable Long ptId) {
        ProgressPtDetailResponse responseBody = ptService.getProgressUserDetails(trainerId,
            ptId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }
}