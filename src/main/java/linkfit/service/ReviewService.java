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
import linkfit.util.JwtUtil;

import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final PtRepository ptRepository;
    private final JwtUtil jwtUtil;

    public ReviewService(ReviewRepository reviewRepository, TrainerRepository trainerRepository,
        UserRepository userRepository, PtRepository ptRepository, JwtUtil jwtUtil) {
        this.reviewRepository = reviewRepository;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.ptRepository = ptRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<ReviewResponse> getAllReviewsByTrainerId(Long trainerId) {
        List<Review> reviews = reviewRepository.findAllByTrainerId(trainerId);
        return reviews.stream()
            .map(Review::toDto)
            .toList();
    }

    public List<ReviewResponse> getMyReviewsByUser(String authorization) {
        Long userId = jwtUtil.parseToken(authorization);
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream()
            .map(Review::toDto)
            .toList();
    }

    public List<ReviewResponse> getMyReviewsByTrainer(String authorization) {
        Long trainerId = jwtUtil.parseToken(authorization);
        return getAllReviewsByTrainerId(trainerId);
    }

    public void addReview(String authorization, ReviewRequest request, Long trainerId) {
        Long userId = jwtUtil.parseToken(authorization);
        User user = userRepository.getReferenceById(userId);
        Trainer trainer = trainerRepository.getReferenceById(trainerId);
        permissionReview(user);
        Review review = new Review(user, trainer, request.content(), request.score());
        reviewRepository.save(review);
    }

    public void deleteReview(String authorization, Long reviewId) {
        Long userId = jwtUtil.parseToken(authorization);
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new NotFoundException("not.found.review"));
        if (!Objects.equals(review.getUser().getId(), userId)) {
            throw new PermissionException("not.owner");
        }
        reviewRepository.delete(review);
    }

    private void permissionReview(User user) {
        ptRepository.findByUserAndStatus(user, PtStatus.COMPLETE)
            .orElseThrow(() -> new PermissionException("review.permission.denied"));
    }
}
