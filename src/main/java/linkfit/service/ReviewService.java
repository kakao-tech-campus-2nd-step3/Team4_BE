package linkfit.service;

import java.util.List;
import linkfit.dto.ReviewRequest;
import linkfit.dto.ReviewResponse;
import linkfit.entity.Review;
import linkfit.entity.Trainer;
import linkfit.entity.User;
import linkfit.repository.ReviewRepository;
import linkfit.repository.TrainerRepository;
import linkfit.repository.UserRepository;
import linkfit.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ReviewService(ReviewRepository reviewRepository, TrainerRepository trainerRepository,
                         UserRepository userRepository, JwtUtil jwtUtil) {
        this.reviewRepository = reviewRepository;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<ReviewResponse> getAllReviewsByTrainerId(Long trainerId) {
        List<Review> reviews = reviewRepository.findAllByTrainerId(trainerId);
        return reviews.stream().map(Review::toDto).toList();
    }

    public List<ReviewResponse> getMyReviewsByUser(String authorization) {
        Long userId = jwtUtil.parseToken(authorization);
        List<Review> reviews = reviewRepository.findAllByUserId(userId);
        return reviews.stream().map(Review::toDto).toList();
    }

    public List<ReviewResponse> getMyReviewsByTrainer(String authorization) {
        Long trainerId = jwtUtil.parseToken(authorization);
        return getAllReviewsByTrainerId(trainerId);
    }

    public void addReview(String authorization, ReviewRequest request, Long trainerId) {
        Long userId = jwtUtil.parseToken(authorization);
        User user = userRepository.getReferenceById(userId);
        Trainer trainer = trainerRepository.getReferenceById(trainerId);

        Review review = new Review(user, trainer, request.content(), request.score());
        reviewRepository.save(review);

    }

    public void deleteReview(String authorization, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(); // 공통 NotfoundException 생성시 추가
        reviewRepository.delete(review);
    }


}
