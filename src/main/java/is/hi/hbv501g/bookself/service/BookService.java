package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.dto.BookRequest;
import is.hi.hbv501g.bookself.dto.BookResponseDTO;
import is.hi.hbv501g.bookself.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<BookResponseDTO> findAll();

    Optional<BookResponseDTO> findById(Long id);

    BookResponseDTO createBook(BookRequest request);

    Optional<BookResponseDTO> updateBook(Long id, BookRequest request);

    Book save(Book book);

    void deleteById(Long id);

    List<BookResponseDTO> searchBooks(String query);

    List<BookResponseDTO> getBooksByGenre(String genre);
}
