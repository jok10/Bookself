package is.hi.hbv501g.bookself.repository;

import is.hi.hbv501g.bookself.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for database operations on Reviews.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Gets all reviews for a specific book.
     *
     * @param bookId the ID of the book
     * @return a list of reviews for the book
     */
    List<Review> findByBookId(Long bookId);

    /**
     * Gets paginated reviews for a specific book.
     *
     * @param bookId the ID of the book
     * @param pageable pagination information
     * @return a page of reviews
     */
    Page<Review> findByBookId(Long bookId, Pageable pageable);

    /**
     * Finds a review on a specific book written by a specific user.
     *
     * @param userId the ID of the user
     * @param bookId the ID of the book
     * @return an Optional containing the review if found
     */
    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);


}
