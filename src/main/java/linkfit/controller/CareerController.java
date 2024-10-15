package linkfit.controller;

import java.util.List;
import linkfit.annotation.Login;
import linkfit.controller.Swagger.TrainerControllerDocs;
import linkfit.dto.CareerRequest;
import linkfit.dto.CareerResponse;
import linkfit.dto.Token;
import linkfit.service.CareerService;
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
@RequestMapping("/api/trainers/careers")
public class CareerController implements TrainerControllerDocs {

    private final CareerService careerService;

    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping
    public ResponseEntity<List<CareerResponse>> getCareer(@Login Token token) {
        List<CareerResponse> list = careerService.getAllCareers(token.id());
        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }

    @PostMapping
    public ResponseEntity<Void> addCareer(@Login Token token,
        @RequestBody List<CareerRequest> request) {
        careerService.addCareer(token.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{careerId}")
    public ResponseEntity<Void> deleteCareer(@Login Token token,
        @PathVariable Long careerId) {
        careerService.deleteCareer(token.id(), careerId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/careers/{trainerId}")
    public ResponseEntity<List<CareerResponse>> getAllCareerByTrainer(
        @PathVariable Long trainerId) {
        List<CareerResponse> list = careerService.getAllCareers(trainerId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }


}
