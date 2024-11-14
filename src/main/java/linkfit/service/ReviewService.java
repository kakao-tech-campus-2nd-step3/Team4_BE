package linkfit.service;

import java.util.List;
import java.util.Objects;
import linkfit.dto.ReviewRequest;
import linkfit.dto.ReviewResponse;
import linkfit.entity.Pt;
import linkfit.entity.Review;
import linkfit.entity.User;
import linkfit.exception.NotFoundException;
import linkfit.exception.PermissionException;
import linkfit.repository.PtRepository;
import linkfit.repository.ReviewRepository;
import linkfit.repository.ScheduleRepository;
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
    private final PtService ptService;
    private final ScheduleRepository scheduleRepository;

    public ReviewService(ReviewRepository reviewRepository, TrainerRepository trainerRepository,
        UserRepository userRepository, PtRepository ptRepository, PtService ptService,
        ScheduleRepository scheduleRepository) {
        this.reviewRepository = reviewRepository;
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
        this.ptRepository = ptRepository;
        this.ptService = ptService;
        this.scheduleRepository = scheduleRepository;
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

    public void addReview(Long userId, ReviewRequest request) {
        User user = userRepository.getReferenceById(userId);
        Pt pt = findPtByUser(user);
        checkReviewWritable(pt);
        Review review = new Review(user, pt.getTrainer(), request);
        reviewRepository.save(review);
        setPtComplete(pt);
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

    private Pt findPtByUser(User user) {
        return ptRepository.findByUserAndStatus(user, PtStatus.APPROVAL)
            .orElseThrow(() -> new NotFoundException("not.found.pt"));
    }

    private void checkReviewWritable(Pt pt) {
        if(pt.getTotalCount() != scheduleRepository.countByPtAndCompleted(pt, Boolean.TRUE))
            throw new PermissionException("review.permission.denied.not.complete.pt");
    }

    private void setPtComplete(Pt pt) {
        pt.complete();
        ptRepository.save(pt);
    }
}