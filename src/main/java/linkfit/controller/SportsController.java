package linkfit.controller;

import jakarta.validation.Valid;
import linkfit.dto.SportsRequest;
import linkfit.service.SportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sports")
public class SportsController {

    private final SportsService sportsService;

    @Autowired
    public SportsController(SportsService sportsService) {
        this.sportsService = sportsService;
    }

    @PostMapping
    public ResponseEntity<String> registerSports(
        @Valid @RequestBody SportsRequest sportsRequest) {
        sportsService.registerSport(sportsRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Registerd Sports: " + sportsRequest.getName());
    }
}
