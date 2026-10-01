package is.hi.hbv501g.bookself.repository;

import is.hi.hbv501g.bookself.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    //Get all reviews for a specific book
    List<Review> findByBook(Long bookId);

    //Get paginated reviews for a specific book
    Page<Review> findByBookId(Long bookId, Pageable pageable);

    //Find a review on a specific book by a specific user.
    Optional<Review> findByUserIdAndBookId(Long userId, Long bookId);

    //Check if user has already reviewed a book
    boolean existsByUserIdAndBookId(Long userId, Long bookId);

}
