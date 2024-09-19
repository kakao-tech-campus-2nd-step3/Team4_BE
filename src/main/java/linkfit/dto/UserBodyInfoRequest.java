package linkfit.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class UserBodyInfoRequest {

    @NotNull
    private int height;

    @NotNull
    private int weight;

    private MultipartFile inbodyImage;

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public MultipartFile getInbodyImage() {
        return inbodyImage;
    }
}
