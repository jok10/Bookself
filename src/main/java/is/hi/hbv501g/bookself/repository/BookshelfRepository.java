package is.hi.hbv501g.bookself.repository;

import is.hi.hbv501g.bookself.model.Bookshelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for database operations on Bookshelfs.
 */
@Repository
public interface BookshelfRepository extends JpaRepository<Bookshelf, Long> {

    /**
     * Finds a user's bookshelf by their user ID.
     *
     * @param userId the ID of the user
     * @return an Optional containing the bookshelf if found
     */
    Optional<Bookshelf> findByUserId(Long userId);

    /**
     * Checks if a specific book is already saved in a user's bookshelf.
     *
     * @param userId the ID of the user
     * @param bookId the ID of the book
     * @return true if the book is on the user's bookshelf, false otherwise
     */
    boolean existsByUser_IdAndBooks_Id(Long userId, Long bookId);

}