package linkfit.controller;

import java.util.List;
import linkfit.controller.Swagger.SportsControllerDocs;
import linkfit.dto.SportsResponse;
import linkfit.service.SportsService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sports")
public class SportsController implements SportsControllerDocs {

    private final SportsService sportsService;

    public SportsController(SportsService sportsService) {
        this.sportsService = sportsService;
    }

    @GetMapping
    public ResponseEntity<List<SportsResponse>> getAllSports(Pageable pageable) {
        List<SportsResponse> responseBody = sportsService.getAllSports(pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }
}
