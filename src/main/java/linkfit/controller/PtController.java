package linkfit.controller;

import java.util.List;
import linkfit.dto.PtResponse;
import linkfit.service.PtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pt")
public class PtController {

    private final PtService ptService;

    @Autowired
    public PtController(PtService ptService) {
        this.ptService = ptService;
    }

    @GetMapping("/trainer")
    public List<PtResponse> getAllPt(
        @RequestHeader("Authorization") String authorization,
        Pageable pageable) {
        return ptService.getAllPts(authorization, pageable);
    }

}
