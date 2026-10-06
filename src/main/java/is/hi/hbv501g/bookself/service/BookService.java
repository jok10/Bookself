package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findAll();

    Optional<Book> findById(Long id);

    Book save(Book book);

    void deleteById(Long id);

    List<Book> searchBooks(String query);

    List<Book> getBooksByGenre(String genre);
}
