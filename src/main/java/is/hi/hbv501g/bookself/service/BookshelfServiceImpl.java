package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Bookshelf;
import is.hi.hbv501g.bookself.repository.BookRepository;
import is.hi.hbv501g.bookself.repository.BookshelfRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Service
public class BookshelfServiceImpl implements BookshelfService {
    private final BookshelfRepository bookshelfRepository;
    private final BookRepository bookRepository;

    @Autowired
    BookshelfServiceImpl(BookshelfRepository bookshelfRepo, BookRepository bookRepo) {
        this.bookshelfRepository = bookshelfRepo;
        this.bookRepository = bookRepo;
    }

    public Optional<Bookshelf> getUserBookshelf(Long userId) {
        return bookshelfRepository.findByUserId(userId);
    }

    public void addBookToShelf(Long userId, Long bookId) {
        getUserBookshelf(userId).ifPresent(bookshelf -> {
            bookRepository.findById(bookId).ifPresent(book -> {
                bookshelf.addBook(book);
                bookshelfRepository.save(bookshelf);
            });
        });
    }

    public void removeBookFromShelf(Long userId, Long bookId) {
        getUserBookshelf(userId).ifPresent(bookshelf -> {
            bookRepository.findById(bookId).ifPresent(book -> {
                bookshelf.removeBook(book);
                bookshelfRepository.save(bookshelf);
            });
        });
    }
}
