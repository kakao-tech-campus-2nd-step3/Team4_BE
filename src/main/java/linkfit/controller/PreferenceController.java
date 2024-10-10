package linkfit.controller;

import java.util.List;
import linkfit.annotation.LoginTrainer;
import linkfit.annotation.LoginUser;
import linkfit.dto.PreferenceRequest;
import linkfit.dto.PreferenceResponse;
import linkfit.service.PreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PostMapping
    public ResponseEntity<Void> registerPreference(@LoginUser Long userId,
        @RequestBody PreferenceRequest request) {
        preferenceService.registerPreference(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PreferenceResponse>> getAllMatchingPossible(
        @LoginTrainer Long trainerId) {
        List<PreferenceResponse> preferences = preferenceService.getAllMatchingPossible(trainerId);
        return ResponseEntity.status(HttpStatus.OK).body(preferences);
    }

    @DeleteMapping("/{preferenceId}")
    public ResponseEntity<Void> deletePreference(@PathVariable Long preferenceId,
        @LoginUser Long userId) {
        preferenceService.deletePreference(preferenceId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}