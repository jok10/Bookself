package is.hi.hbv501g.bookself.repository;

import is.hi.hbv501g.bookself.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
