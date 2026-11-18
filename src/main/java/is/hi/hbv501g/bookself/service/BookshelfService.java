package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Bookshelf;

import java.util.Optional;

/**
 * Service interface for managing user bookshelves.
 */
public interface BookshelfService {

    /**
     * Retrieves the bookshelf that belongs to a specific user.
     *
     * @param userId the unique ID of the user
     * @return the user's Bookshelf entity
     */
    Bookshelf getUserBookshelf(Long userId);

    /**
     * Adds a specific book to a user's bookshelf.
     *
     * @param userId the unique ID of the user
     * @param bookId the unique ID of the book to be added
     * @return the updated Bookshelf entity
     */
    Bookshelf addBookToShelf(Long userId, Long bookId);

    /**
     * Removes a specific book from a user's bookshelf.
     *
     * @param userId the unique ID of the user
     * @param bookId the unique ID of the book to be removed
     * @return the updated Bookshelf entity
     */
    Bookshelf removeBookFromShelf(Long userId, Long bookId);
}
