package linkfit.controller;

import jakarta.validation.Valid;
import java.util.List;
import linkfit.dto.PtSuggestionRequest;
import linkfit.dto.PtSuggestionResponse;
import linkfit.dto.PtSuggestionUpdateRequest;
import linkfit.dto.PtTrainerResponse;
import linkfit.dto.PtUserResponse;
import linkfit.dto.TrainerPtResponse;
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
    public ResponseEntity<List<TrainerPtResponse>> getAllPt(
        @RequestHeader("Authorization") String authorization,
        Pageable pageable) {
        List<TrainerPtResponse> responseBody = ptService.getAllPt(authorization, pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/trainer/suggest")
    public ResponseEntity<List<PtSuggestionResponse>> getAllPtSuggestion(
        @RequestHeader("Authorization") String authorization,
        Pageable pageable) {
        List<PtSuggestionResponse> responseBody = ptService.getAllPtSuggestion(authorization, pageable);
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
    public ResponseEntity<Void> suggestPt(
        @RequestHeader("Authorization") String authorization,
        @Valid @RequestBody PtSuggestionRequest ptSuggestionRequest) {
        ptService.suggestPt(authorization, ptSuggestionRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @DeleteMapping("/{ptId}")
    public ResponseEntity<Void> recallPtSuggestion(
        @RequestHeader("Authorization") String authorization,
        @PathVariable Long ptId) {
        ptService.recallPtSuggestion(authorization, ptId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PutMapping("/{ptId}")
    public ResponseEntity<Void> updatePtSuggestion(
        @RequestHeader("Authorization") String authorization,
        @PathVariable Long ptId,
        @Valid @RequestBody PtSuggestionUpdateRequest ptSuggestionUpdateRequest) {
        ptService.updatePtSuggestion(authorization, ptId, ptSuggestionUpdateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping("/{ptId}/user")
    public ResponseEntity<PtTrainerResponse> getPtTrainerProfile(
        @RequestHeader("Authorization") String authorization,
        @PathVariable Long ptId) {
        PtTrainerResponse responseBody = ptService.getPtTrainerProfile(authorization, ptId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/{ptId}/trainer")
    public ResponseEntity<PtUserResponse> getPtUserProfile(
        @RequestHeader("Authorization") String authorization,
        @PathVariable Long ptId) {
        PtUserResponse responseBody = ptService.getPtUserProfile(authorization, ptId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }
}
