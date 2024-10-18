package linkfit.controller;

import java.util.List;
import linkfit.annotation.Login;
import linkfit.controller.Swagger.ReviewControllerDocs;
import linkfit.dto.ReviewRequest;
import linkfit.dto.ReviewResponse;
import linkfit.dto.Token;
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
        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReviewResponse>> getMyReviewByUser(@Login Token token) {
        List<ReviewResponse> list = reviewService.getMyReviewsByUserId(token.id());
        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }

    @GetMapping("/trainer")
    public ResponseEntity<List<ReviewResponse>> getMyReviewByTrainer(@Login Token token) {
        List<ReviewResponse> list = reviewService.getAllReviewsByTrainerId(token.id());
        return ResponseEntity.status(HttpStatus.OK)
            .body(list);
    }

    @PostMapping("/{trainerId}")
    public ResponseEntity<Void> addReview(@Login Token token,
        @RequestBody ReviewRequest request, @PathVariable Long trainerId) {
        reviewService.addReview(token.id(), request, trainerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReviewById(@Login Token token,
        @PathVariable Long reviewId) {
        reviewService.deleteReview(token.id(), reviewId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}