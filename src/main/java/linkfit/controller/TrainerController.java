package linkfit.controller;


import java.util.List;
import linkfit.annotation.LoginTrainer;
import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.dto.TrainerProfileResponse;
import linkfit.entity.Trainer;
import linkfit.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainer")
public class TrainerController {

    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping("/career")
    public ResponseEntity<List<CareerResponse>> getMyCareer(@LoginTrainer Trainer trainer) {
        List<CareerResponse> list = trainerService.getCareers(trainer.getId());
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("/career")
    public ResponseEntity<Void> addMyCareer(@LoginTrainer Trainer trainer,
        @RequestBody CareerRequest req) {
        trainerService.addCareer(trainer, req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{careerId}")
    public ResponseEntity<Void> deleteCareer(@PathVariable Long careerId) {
        trainerService.deleteCareer(careerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/career/{trainerId}")
    public ResponseEntity<List<CareerResponse>> getTrainerCareer(@PathVariable Long trainerId) {
        List<CareerResponse> list = trainerService.getCareers(trainerId);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/profile/{trainerId}")
    public ResponseEntity<TrainerProfileResponse> getTrainerProfile(@PathVariable Long trainerId) {
        TrainerProfileResponse res = trainerService.getProfile(trainerId);
        return ResponseEntity.ok().body(res);
    }


}
