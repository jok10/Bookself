package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.dto.ReviewResponseDTO;
import is.hi.hbv501g.bookself.model.Review;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Service interface for managing book reviews, ratings, and pagination.
 */
public interface ReviewService {

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId the unique ID of the book
     * @return a list of review response DTOs
     */
    List<ReviewResponseDTO> getReviewsByBook(Long bookId);

    /**
     * Adds a new review or updates an existing one for a book by a specific user.
     *
     * @param userId the unique ID of the user writing the review
     * @param bookId the unique ID of the book being reviewed
     * @param rating the rating given to the book
     * @param text the text of the review
     * @return the saved review response DTO
     */
    ReviewResponseDTO addOrUpdateReview(Long userId, Long bookId, double rating, String text);

    /**
     * Retrieves a paginated list of reviews for a book.
     *
     * @param bookId the unique ID of the book
     * @param page the page number to retrieve (0-indexed)
     * @param size the number of items per page
     * @return a Page containing review response DTOs
     */
    Page<ReviewResponseDTO> getPaginatedReviews(Long bookId, int page, int size);

    /**
     * Deletes a user's review for a specific book.
     *
     * @param userId the unique ID of the user who wrote the review
     * @param bookId the unique ID of the book
     */
    void deleteReview(Long userId, Long bookId);
}
