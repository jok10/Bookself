package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByBook(Long bookId);
    Review addOrUpdateReview(Long userId, Long bookId, double rating, String text);
    Page<Review> getPaginatedReviews(Long bookId, int page, int size);
}
