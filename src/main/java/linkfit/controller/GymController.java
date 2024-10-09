package linkfit.controller;

import jakarta.validation.Valid;
import java.util.List;
import linkfit.dto.GymDescriptionRequest;
import linkfit.dto.GymDetailResponse;
import linkfit.dto.GymLocationResponse;
import linkfit.dto.GymRegisterRequest;
import linkfit.dto.GymSearchRequest;
import linkfit.dto.GymSearchResponse;
import linkfit.dto.GymTrainersResponse;
import linkfit.service.GymService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<GymSearchResponse> searchGymByKeyword(
        @RequestBody GymSearchRequest request, Pageable pageable) {
        System.out.println(request.keyword());
        GymSearchResponse responseBody = gymService.findAllByKeyword(request.keyword(), pageable);
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

    @PostMapping("/register")
    public ResponseEntity<Void> sendGymRegistrationRequest(
        @RequestHeader("Authorization") String authorization,
        @Valid @RequestBody GymRegisterRequest gymRegisterRequest) {
        gymService.sendGymRegistrationRequest(authorization, gymRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{gymId}")
    public ResponseEntity<Void> updateGymInfo(@PathVariable Long gymId,
        @RequestHeader("Authorization") String authorization,
        @RequestPart("description") GymDescriptionRequest gymDescriptionRequest,
        @RequestPart(value = "images", required = false) List<MultipartFile> gymImages) {
        gymService.updateGym(gymId, authorization, gymDescriptionRequest, gymImages);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
