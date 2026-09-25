package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> searchBooks(String query){
        if (query == null || query.trim().isEmpty()){
            return Collections.emptyList(); // Returns []
            //or we can send an error this way:
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Search query cannot be empty");
        }
        List<Book> books = bookRepository.findAllByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query.trim(), query.trim());
        return books;
        // returns empty if no books are found. But we can also do it this way and throw an exception:
        /*
        List<Book> books = bookRepository.findAllByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query.trim(), query.trim());

        if (books.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No books were found matching: " + query);
        }

        return books
         */

        /*
        To implement:
        getBooksByGenre
        getBooksByMinRating
        createBook
        updateBook
         */
    }
}
