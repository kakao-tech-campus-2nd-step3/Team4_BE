package linkfit.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import linkfit.dto.PreferenceRequest;
import linkfit.dto.PreferenceResponse;
import linkfit.service.PreferenceService;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PostMapping
    public ResponseEntity<Void> registerPreference(@RequestHeader("Authorization") String authorization, PreferenceRequest request) {
        preferenceService.registerPreference(authorization, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PreferenceResponse>> getAllPreference(@RequestHeader("Authorization") String authorization) {
        List<PreferenceResponse> preferences = preferenceService.getAllPreference(authorization);
        return ResponseEntity.status(HttpStatus.OK).body(preferences);
    }
}
