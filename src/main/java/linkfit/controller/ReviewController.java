package linkfit.controller;

import java.util.List;
import linkfit.annotation.LoginTrainer;
import linkfit.annotation.LoginUser;
import linkfit.controller.Swagger.ReviewControllerDocs;
import linkfit.dto.ReviewRequest;
import linkfit.dto.ReviewResponse;
import linkfit.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController implements ReviewControllerDocs {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{trainerId}")
    public ResponseEntity<List<ReviewResponse>> getTrainerReviews(@PathVariable Long trainerId) {
        List<ReviewResponse> list = reviewService.getAllReviewsByTrainerId(trainerId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReviewResponse>> getMyReviewByUser(@LoginUser Long userId) {
        List<ReviewResponse> list = reviewService.getMyReviewsByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<ReviewResponse>> getMyReviewByTrainer(@LoginTrainer Long trainerId) {
        List<ReviewResponse> list = reviewService.getAllReviewsByTrainerId(trainerId);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping("/{trainerId}")
    public ResponseEntity<Void> addReview(@LoginUser Long userId,
        @RequestBody ReviewRequest request, @PathVariable("trainerId") Long trainerId) {
        reviewService.addReview(userId, request, trainerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReviewById(@LoginTrainer Long userId,
        @PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(userId, reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}