package linkfit.controller;

import java.util.List;
import linkfit.dto.GymDetailResponse;
import linkfit.dto.GymLocationResponse;
import linkfit.dto.GymSearchResponse;
import linkfit.dto.GymTrainersResponse;
import linkfit.service.GymService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

    private final GymService gymService;

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    @GetMapping("/{gymId}")
    public ResponseEntity<GymDetailResponse> getGymDetails(@PathVariable Long gymId) {
        GymDetailResponse responseBody = gymService.getGymDetails(gymId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/search")
    public ResponseEntity<GymSearchResponse> searchGymByKeyword(@RequestBody String keyword,
        Pageable pageable) {
        GymSearchResponse responseBody = gymService.findAllByKeyword(keyword, pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/{gymId}/trainers")
    public ResponseEntity<GymTrainersResponse> getGymTrainers(@PathVariable Long gymId,
        Pageable pageable) {
        GymTrainersResponse responseBody = gymService.getGymTrainers(gymId, pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<GymLocationResponse>> getGymLocations() {
        List<GymLocationResponse> responseBody = gymService.getGymLocations();
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }
}
