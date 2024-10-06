package linkfit.dto;

import java.time.LocalDateTime;

public record ReviewResponse(Long reviewId, String content, LocalDateTime createDate, int score) {

}