package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByBook(Long bookId);
    Review addOrUpdateReview(Long userId, Long bookId, int rating, String text);
}
