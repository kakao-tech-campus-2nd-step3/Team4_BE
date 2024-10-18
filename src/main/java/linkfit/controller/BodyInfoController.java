package linkfit.controller;

import java.util.List;
import linkfit.annotation.Login;
import linkfit.controller.Swagger.BodyInfoControllerDocs;
import linkfit.dto.BodyInfoResponse;
import linkfit.dto.Token;
import linkfit.service.BodyInfoService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bodyInfos")
public class BodyInfoController implements BodyInfoControllerDocs {

    private final BodyInfoService bodyInfoService;

    public BodyInfoController(BodyInfoService bodyInfoService) {
        this.bodyInfoService = bodyInfoService;
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> registerBodyInfo(
        @Login Token token,
        @RequestPart(value = "inbodyImage") MultipartFile inbodyImage) {
        bodyInfoService.registerBodyInfo(token.id(), inbodyImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<BodyInfoResponse>> getAllBodyInfo(@Login Token token,
        Pageable pageable) {
        List<BodyInfoResponse> responseBody = bodyInfoService.getAllBodyInfo(token.id(),
            pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @DeleteMapping("/{bodyInfoId}")
    public ResponseEntity<Void> deleteBodyInfo(@Login Token token,
        @PathVariable Long bodyInfoId) {
        bodyInfoService.deleteBodyInfo(token.id(), bodyInfoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
