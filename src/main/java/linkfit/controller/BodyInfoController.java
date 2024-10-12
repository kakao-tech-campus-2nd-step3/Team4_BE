package linkfit.controller;

import java.util.List;

import linkfit.annotation.LoginUser;
import linkfit.controller.Swagger.BodyInfoControllerDocs;
import linkfit.dto.BodyInfoResponse;
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
@RequestMapping("/api/users/bodyInfos")
public class BodyInfoController implements BodyInfoControllerDocs {

    private final BodyInfoService bodyInfoService;

    public BodyInfoController(BodyInfoService bodyInfoService) {
        this.bodyInfoService = bodyInfoService;
    }

    @PostMapping(value = "/bodyInfo", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> registerBodyInfo(
        @LoginUser Long userId,
        @RequestPart(value = "inbodyImage") MultipartFile inbodyImage) {
        bodyInfoService.registerBodyInfo(userId, inbodyImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/bodyInfo")
    public ResponseEntity<List<BodyInfoResponse>> getAllBodyInfo(@LoginUser Long userId,
        Pageable pageable) {
        List<BodyInfoResponse> responseBody = bodyInfoService.getAllBodyInfo(userId,
            pageable);
        return ResponseEntity.status(HttpStatus.OK)
            .body(responseBody);
    }

    @DeleteMapping("/bodyInfo/{bodyInfoId}")
    public ResponseEntity<Void> deleteBodyInfo(@LoginUser Long userId,
        @PathVariable Long bodyInfoId) {
        bodyInfoService.deleteBodyInfo(userId, bodyInfoId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
