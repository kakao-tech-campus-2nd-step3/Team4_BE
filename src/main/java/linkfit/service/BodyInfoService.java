package linkfit.service;

import java.util.List;
import java.util.Objects;
import linkfit.dto.BodyInfoResponse;
import linkfit.entity.BodyInfo;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.BodyInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BodyInfoService {

    private final BodyInfoRepository bodyInfoRepository;
    private final UserService userService;
    private final ImageUploadService imageUploadService;

    public BodyInfoService(BodyInfoRepository bodyInfoRepository, UserService userService,
        ImageUploadService imageUploadService) {
        this.bodyInfoRepository = bodyInfoRepository;
        this.userService = userService;
        this.imageUploadService = imageUploadService;
    }

    public void registerBodyInfo(Long userId, MultipartFile profileImage) {
        User user = userService.getUser(userId);
        String imageUrl = imageUploadService.saveImage(profileImage);
        BodyInfo bodyInfo = new BodyInfo(user, imageUrl);
        bodyInfoRepository.save(bodyInfo);
    }

    public List<BodyInfoResponse> getAllBodyInfo(Long userId, Pageable pageable) {
        User user = userService.getUser(userId);
        Page<BodyInfo> bodyInfos = bodyInfoRepository.findAllByUserId(user.getId(), pageable);
        return bodyInfos.stream()
            .map(BodyInfo::toDto)
            .toList();
    }

    public BodyInfo getBodyInfo(Long id) {
        return bodyInfoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("not.found.bodyinfo"));
    }

    public void deleteBodyInfo(Long userId, Long bodyInfoId) {
        BodyInfo bodyInfo = getBodyInfo(bodyInfoId);
        validateBodyInfoOwnership(bodyInfo, userId);
        bodyInfoRepository.delete(bodyInfo);
    }

    private void validateBodyInfoOwnership(BodyInfo bodyInfo, Long userId) {
        Long ownerId = bodyInfo.getUser().getId();
        if (!Objects.equals(ownerId, userId)) {
            throw new PermissionException("bodyinfo.permission.denied");
        }
    }
}
