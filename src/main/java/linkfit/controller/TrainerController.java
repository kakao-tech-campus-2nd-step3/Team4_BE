package linkfit.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/career")
    public ResponseEntity<List<CareerResponse>> getMyCareer(
        @RequestHeader("Authorization") String authorization) {
        List<CareerResponse> list = trainerService.getCareers(authorization);
        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }

    @PostMapping("/career")
    public ResponseEntity<Void> addMyCareer(
        @RequestHeader("Authorization") String authorization,
        @RequestBody CareerRequest request) {
        trainerService.addCareer(authorization, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{careerId}")
    public ResponseEntity<Void> deleteCareer(
        @RequestHeader("Authorization") String authorization,
        @PathVariable("careerId") Long careerId) {
        trainerService.deleteCareer(authorization, careerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/career/{trainerId}")
    public ResponseEntity<List<CareerResponse>> getTrainerCareer(
        @PathVariable("trainerId") Long trainerId) {
        List<CareerResponse> list = trainerService.getCareersByTrainerId(trainerId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(
        @PathVariable("trainerId") Long trainerId) {
        TrainerProfileResponse res = trainerService.getProfile(trainerId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(res);
    }

    @GetMapping("/profile")
    public ResponseEntity<TrainerProfileResponse> getMyProfile(
        @RequestHeader("Authorization") String authorization) {
        TrainerProfileResponse responseBody = trainerService.getMyProfile(authorization);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

}
