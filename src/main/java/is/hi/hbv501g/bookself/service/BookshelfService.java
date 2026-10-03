package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Bookshelf;

import java.util.Optional;

/**
 * + getUserBookshelf(userId: Long): Bookshelf
 * + addBookToShelf(userId: Long, bookId: Long): Bookshelf
 * + removeBookFromShelf(userId: Long, bookId: Long): Bookshelf
 */
public interface BookshelfService {
    Bookshelf getUserBookshelf(Long userId);
    Bookshelf addBookToShelf(Long userId, Long bookId);
    Bookshelf removeBookFromShelf(Long userId, Long bookId);
}
