package linkfit.service;

import java.util.List;
import java.util.Objects;
import linkfit.dto.ReviewRequest;
import linkfit.dto.ReviewResponse;
import linkfit.entity.Review;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.PtRepository;
import linkfit.repository.ReviewRepository;
import linkfit.repository.TrainerRepository;
import linkfit.repository.UserRepository;
import linkfit.status.PtStatus;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final PtRepository ptRepository;

    public ReviewService(ReviewRepository reviewRepository, TrainerRepository trainerRepository,
        UserRepository userRepository, PtRepository ptRepository) {
        this.reviewRepository = reviewRepository;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.ptRepository = ptRepository;
    }

    public List<ReviewResponse> getAllReviewsByTrainerId(Long trainerId) {
        List<Review> reviews = reviewRepository.findAllByTrainerId(trainerId);
        return reviews.stream()
            .map(Review::toDto)
            .toList();
    }

    public List<ReviewResponse> getMyReviewsByUserId(Long userId) {
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream()
            .map(Review::toDto)
            .toList();
    }

    public void addReview(Long userId, ReviewRequest request, Long trainerId) {
        User user = userRepository.getReferenceById(userId);
        Trainer trainer = trainerRepository.getReferenceById(trainerId);
        checkPTCompletion(user);
        Review review = new Review(user, trainer, request);
        reviewRepository.save(review);
    }

    public void deleteReview(Long userId, Long reviewId) {
        Review review = getReviewById(reviewId);
        validateReviewAuthor(review, userId);
        reviewRepository.delete(review);
    }

    private Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundException("not.found.review"));
    }

    private void validateReviewAuthor(Review review, Long userId) {
        Long authorId = review.getUser().getId();
        if (!Objects.equals(authorId, userId)) {
            throw new PermissionException("not.owner");
        }
    }

    private void checkPTCompletion(User user) {
        ptRepository.findByUserAndStatus(user, PtStatus.COMPLETE)
            .orElseThrow(() -> new PermissionException("review.permission.denied"));
    }
}