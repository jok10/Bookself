package is.hi.hbv501g.bookself.repository;

import is.hi.hbv501g.bookself.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByTitleContainingIgnoreCase(String title);
    List<Book> findAllByAuthorContainingIgnoreCase(String author);
    List<Book> findAllByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    boolean existsByIsbn(String isbn);
    List<Book> findAllByGenreIgnoreCase(String genre);
    List<Book> findAllByAvgRatingGreaterThanEqual(double minRating);
}