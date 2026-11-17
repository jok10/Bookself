package is.hi.hbv501g.bookself.repository;

import is.hi.hbv501g.bookself.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for database operations on Books.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Finds books where titles contain the specified search string, ignoring case.
     *
     * @param title the title keyword to search for
     * @return a list of matching books
     */
    List<Book> findAllByTitleContainingIgnoreCase(String title);
    /**
     * Finds books written by an author that matches the specified search string, ignoring case.
     *
     * @param author the author name keyword to search for
     * @return a list of matching books
     */
    List<Book> findAllByAuthorContainingIgnoreCase(String author);
    /**
     * Finds all books published in a specific year.
     *
     * @param year the publication year
     * @return a list of books from that year
     */
    List<Book> findAllByYear(int year);
    /**
     * Finds books where either the title or the author matches the search keyword, ignoring case.
     *
     * @param title the title keyword
     * @param author the author keyword
     * @return a list of matching books
     */
    List<Book> findAllByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    /**
     * Finds books matching a specific genre, ignoring case.
     *
     * @param genre the genre name
     * @return a list of books in that genre
     */
    List<Book> findAllByGenreIgnoreCase(String genre);

    /**
     * Finds books with an average rating greater than or equal to the specified threshold.
     *
     * @param minRating the minimum rating score
     * @return a list of highly rated books
     */
    List<Book> findAllByAvgRatingGreaterThanEqual(double minRating);
}