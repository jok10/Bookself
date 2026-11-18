package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Bookshelf;
import is.hi.hbv501g.bookself.repository.BookRepository;
import is.hi.hbv501g.bookself.repository.BookshelfRepository;
import is.hi.hbv501g.bookself.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Implementation of the BookshelfService interface for managing user bookshelves.
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

    @Override
    public Bookshelf getUserBookshelf(Long userId) {
        return bookshelfRepository.findByUserId(userId).orElseGet(() -> {
            // If the user doesn't have a bookshelf, create one
            return userRepository.findById(userId).map(user -> {
                Bookshelf newBookshelf = new Bookshelf(user);
                return bookshelfRepository.save(newBookshelf);
            }).orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found."));
        });
    }

    @Override
    public Bookshelf addBookToShelf(Long userId, Long bookId) {
        Bookshelf bookshelf = getUserBookshelf(userId);

        if (!bookRepository.existsById(bookId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Book not found.");
        }

        if (bookshelfRepository.existsByUser_IdAndBooks_Id(userId, bookId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Book is already in the user's bookshelf.");
        }

        bookRepository.findById(bookId).ifPresent(book -> {
            bookshelf.addBook(book);
            bookshelfRepository.save(bookshelf);
        });
        return bookshelf;
    }

    @Override
    public Bookshelf removeBookFromShelf(Long userId, Long bookId) {
        Bookshelf bookshelf = getUserBookshelf(userId);

        if (!bookRepository.existsById(bookId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Book not found.");
        }

        if (!bookshelfRepository.existsByUser_IdAndBooks_Id(userId, bookId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Book is not in the user's bookshelf.");
        }

        bookRepository.findById(bookId).ifPresent(book -> {
            bookshelf.removeBook(book);
            bookshelfRepository.save(bookshelf);
        });
        return bookshelf;
    }
}
