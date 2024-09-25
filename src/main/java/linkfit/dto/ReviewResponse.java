package linkfit.dto;

import java.time.LocalDateTime;
import linkfit.entity.Review;

public record ReviewResponse(Long reviewId, String content, LocalDateTime date, int score) {


}
