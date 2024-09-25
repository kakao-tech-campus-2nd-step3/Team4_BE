package linkfit.controller;

import java.util.List;

import linkfit.dto.ReviewResponse;
import linkfit.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<List<ReviewResponse>> getTrainerReviews(
            @PathVariable("trainerId") Long trainerId) {
        List<ReviewResponse> list = reviewService.getAllReviewsByTrainerId(trainerId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReviewResponse>> getMyReviewByUser(
            @RequestHeader("Authorization") String authorization) {
        List<ReviewResponse> list = reviewService.getMyReviewsByUser(authorization);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }


    @GetMapping("/trainer")
    public ResponseEntity<List<ReviewResponse>> getMyReviewByTrainer(
            @RequestHeader("Authorization") String authorization) {
        List<ReviewResponse> list = reviewService.getMyReviewsByTrainer(authorization);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReviewById(
            @RequestHeader("Authorization") String authorization,
            @PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(authorization, reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }


}
