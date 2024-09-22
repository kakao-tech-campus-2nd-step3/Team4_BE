package linkfit.controller;

import java.util.List;
import linkfit.dto.SportsResponse;
import linkfit.service.SportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public List<SportsResponse> getAllSports(Pageable pageable) {
        return sportsService.getAllSports(pageable);
    }
}
