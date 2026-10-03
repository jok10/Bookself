package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Bookshelf;

import java.util.Optional;

/**
 * + getUserBookshelf(userId: Long): Bookshelf
 * + addBookToShelf(userId: Long, bookId: Long): Bookshelf
 * + removeBookFromShelf(userId: Long, bookId: Long): Bookshelf
 */
public interface BookshelfService {
    Optional<Bookshelf> getUserBookshelf(Long userId);
    void addBookToShelf(Long userId, Long bookId);
    void removeBookFromShelf(Long userId, Long bookId);
}
