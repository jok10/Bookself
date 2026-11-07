package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.dto.ReviewResponseDTO;
import is.hi.hbv501g.bookself.model.Review;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    List<ReviewResponseDTO> getReviewsByBook(Long bookId);
    ReviewResponseDTO addOrUpdateReview(Long userId, Long bookId, double rating, String text);
    Page<ReviewResponseDTO> getPaginatedReviews(Long bookId, int page, int size);
}
