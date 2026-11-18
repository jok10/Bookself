package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.dto.BookRequest;
import is.hi.hbv501g.bookself.dto.BookResponseDTO;
import is.hi.hbv501g.bookself.model.Book;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing books.
 */
public interface BookService {

    /**
     * Retrieves all books in the system as response DTOs.
     *
     * @return a list of all book response DTOs
     */
    List<BookResponseDTO> findAll();

    /**
     * Finds a specific book by its unique ID.
     *
     * @param id the unique identifier of the book
     * @return an Optional containing the book response DTO if found, or empty otherwise
     */
    Optional<BookResponseDTO> findById(Long id);

    /**
     * Creates and saves a new book from the provided request.
     *
     * @param request the book request DTO containing book details
     * @return the created book response DTO
     */
    BookResponseDTO createBook(BookRequest request);

    /**
     * Updates an existing book by its ID with new details.
     *
     * @param id the ID of the book to update
     * @param request the book request DTO with updated information
     * @return an Optional containing the updated book response DTO if found, or empty otherwise
     */
    Optional<BookResponseDTO> updateBook(Long id, BookRequest request);

    /**
     * Saves or updates a raw Book entity directly.
     *
     * @param book the Book entity to save
     * @return the saved Book entity
     */
    Book save(Book book);

    /**
     * Deletes a book by its unique ID.
     *
     * @param id the ID of the book to delete
     */
    void deleteById(Long id);

    /**
     * Searches for books matching a keyword or query string.
     *
     * @param query the keyword or query string
     * @return a list of matching book response DTOs
     */
    List<BookResponseDTO> searchBooks(String query);

    /**
     * Retrieves all books of a specific genre.
     *
     * @param genre the genre name
     * @return a list of book response DTOs in that genre
     */
    List<BookResponseDTO> getBooksByGenre(String genre);
}
