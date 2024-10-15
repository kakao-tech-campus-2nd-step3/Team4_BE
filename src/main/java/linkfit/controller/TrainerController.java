package linkfit.controller;

import java.util.List;
import linkfit.annotation.Login;
import linkfit.controller.Swagger.TrainerControllerDocs;
import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.dto.TrainerProfileResponse;
import linkfit.service.TrainerService;
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
@RequestMapping("/api/trainers")
public class TrainerController implements TrainerControllerDocs {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(@PathVariable Long trainerId) {
        TrainerProfileResponse responseBody = trainerService.getProfile(trainerId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/profile")
    public ResponseEntity<TrainerProfileResponse> getMyProfile(@Login Long trainerId) {
        TrainerProfileResponse responseBody = trainerService.getMyProfile(trainerId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }
}