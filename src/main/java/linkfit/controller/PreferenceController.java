package linkfit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import linkfit.dto.PreferenceRequest;
import linkfit.service.PreferenceService;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {
	
	private final PreferenceService preferenceService;
	
	public PreferenceController(PreferenceService preferenceService) {
		this.preferenceService = preferenceService;
	}
	
	@PostMapping
	public ResponseEntity<Void> preferenceRegister(@RequestHeader("Authorization") String authorization,
			PreferenceRequest request){
		preferenceService.registerPreference(authorization, request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
}
