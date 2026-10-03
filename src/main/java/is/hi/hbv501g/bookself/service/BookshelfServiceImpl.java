package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Bookshelf;
import is.hi.hbv501g.bookself.repository.BookRepository;
import is.hi.hbv501g.bookself.repository.BookshelfRepository;
import is.hi.hbv501g.bookself.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    BookshelfServiceImpl(BookshelfRepository bookshelfRepo, BookRepository bookRepo, UserRepository userRepo) {
        this.bookshelfRepository = bookshelfRepo;
        this.bookRepository = bookRepo;
        this.userRepository = userRepo;
    }

    public Bookshelf getUserBookshelf(Long userId) {
        return bookshelfRepository.findByUserId(userId).orElseGet(() -> {
            // If the user doesn't have a bookshelf, create one
            return userRepository.findById(userId).map(user -> {
                Bookshelf newBookshelf = new Bookshelf(user);
                return bookshelfRepository.save(newBookshelf);
            }).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        });
    }

    public Bookshelf addBookToShelf(Long userId, Long bookId) {
        Bookshelf bookshelf = getUserBookshelf(userId);
        bookRepository.findById(bookId).ifPresent(book -> {
            bookshelf.addBook(book);
            bookshelfRepository.save(bookshelf);
        });
        return bookshelf;
    }

    public Bookshelf removeBookFromShelf(Long userId, Long bookId) {
        Bookshelf bookshelf = getUserBookshelf(userId);
        bookRepository.findById(bookId).ifPresent(book -> {
            bookshelf.removeBook(book);
            bookshelfRepository.save(bookshelf);
        });
        return bookshelf;
    }
}
