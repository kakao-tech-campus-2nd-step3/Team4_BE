package linkfit.controller;

import java.util.List;
import linkfit.annotation.LoginTrainer;
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

    @GetMapping("/career")
    public ResponseEntity<List<CareerResponse>> getCareer(@LoginTrainer Long trainerId) {
        List<CareerResponse> list = trainerService.getCareers(trainerId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }

    @PostMapping("/career")
    public ResponseEntity<Void> addCareer(@LoginTrainer Long trainerId,
        @RequestBody List<CareerRequest> request) {
        trainerService.addCareer(trainerId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }

    @DeleteMapping("/{careerId}")
    public ResponseEntity<Void> deleteCareer(@LoginTrainer Long trainerId,
        @PathVariable("careerId") Long careerId) {
        trainerService.deleteCareer(trainerId, careerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping("/{trainerId}/careers")
    public ResponseEntity<List<CareerResponse>> getAllCareerByTrainer(
        @PathVariable("trainerId") Long trainerId) {
        List<CareerResponse> list = trainerService.getCareersByTrainerId(trainerId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(@PathVariable("trainerId") Long trainerId) {
        TrainerProfileResponse responseBody = trainerService.getProfile(trainerId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @GetMapping("/profile")
    public ResponseEntity<TrainerProfileResponse> getMyProfile(@LoginTrainer Long trainerId) {
        TrainerProfileResponse responseBody = trainerService.getMyProfile(trainerId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }
}