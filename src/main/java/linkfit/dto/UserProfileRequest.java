package linkfit.dto;

import org.springframework.web.multipart.MultipartFile;

public record UserProfileRequest(MultipartFile profileImage, String local, String name) {}
