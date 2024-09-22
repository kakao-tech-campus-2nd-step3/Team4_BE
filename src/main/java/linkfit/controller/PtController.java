package linkfit.controller;

import java.util.List;
import linkfit.dto.TrainerPtResponse;
import linkfit.dto.UserPtResponse;
import linkfit.service.PtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<TrainerPtResponse> getAllPt(
        @RequestHeader("Authorization") String authorization,
        Pageable pageable) {
        return ptService.getAllPt(authorization, pageable);
    }

    @GetMapping("/user")
    public ResponseEntity<UserPtResponse> getMyPt(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(ptService.getMyPt(authorization));
    }
}
