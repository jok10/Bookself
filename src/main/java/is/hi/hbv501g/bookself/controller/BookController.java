package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.BookRequest;
import is.hi.hbv501g.bookself.dto.BookResponseDTO;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.service.BookService;
import is.hi.hbv501g.bookself.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.net.URI;
import java.util.List;

/**
 * REST controller responsible for managing books.
 * Handles public viewing, searching, and filtering of books,
 * as well as secured administrative operations (create, update, delete).
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public BookController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    //Helper method.

    /**
     * Helper method to verify that the user making the request is an admin.
     * Extracts the user ID from the JWT token and checks their role.
     *
     * @param jwt the security token of the authenticated user
     * @throws ResponseStatusException with 403 Forbidden if the user is not an admin
     */
    private void verifyAdmin(Jwt jwt) {

        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        User currentUser = userService.getUserById(userId);

        if (!currentUser.isAdmin()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Admin access required.");
        }
    }

    //Get books

    /**
     * Retrieves a list of all books available in system.
     * @return a list of BookResponseDTO objects representing all books
     */
    @GetMapping
    public List<BookResponseDTO> getAllBooks() {
        return bookService.findAll();
    }


    /**
     * Retrieves a specific book by its unique id.
     *
     * @param id the unique id of the book.
     * @return a ResponseEntity containing the BookResponseDTO if found, or 404 Not Found otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Search and filter

    /**
     * Searches for books matching a specific search query string. Can be title of book or name of author.
     *
     * @param query the keyword or phrase to search for
     * @return a ResponseEntity containing a list of matching BookResponseDTOs
     */

    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(@RequestParam("query") String query){
        return ResponseEntity.ok(bookService.searchBooks(query));
    }

    /**
     * Filters and retrieves books by a specific genre (case-insensitive).
     *
     * @param genre the genre string to filter by
     * @return a ResponseEntity containing a list of books belonging to the given genre
     */
    @GetMapping("/genre")
    public ResponseEntity<List<BookResponseDTO>> getBooksByGenre(@RequestParam("genre") String genre){
        return ResponseEntity.ok(bookService.getBooksByGenre(genre));
    }


    //Book management

    /**
     * Creates a new book in the system.
     * Restricted to admin users.
     *
     * @param request the book details payload
     * @param jwt the security token of the user attempting the operation
     * @return a 201 Created response containing the saved BookResponseDTO and its resource URI location
     */

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequest request, @AuthenticationPrincipal Jwt jwt) {
        verifyAdmin(jwt);
        BookResponseDTO saved = bookService.createBook(request);
        URI location = URI.create("/api/books/" + saved.getId());

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Updates existing book's information. Only fields provided in the request payload will be updated.
     * Restricted to admin users.
     * @param id the unique identifier of the book.
     * @param request The BookRequest DTO that contains the details to be updated
     * @param jwt the security token of the user attempting the operation
     * @return A ResponseEntity with status 200 ok and the updated BookResponseDTO or status 404 not found if no book with given ID exists.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @RequestBody BookRequest request, @AuthenticationPrincipal Jwt jwt) {
        verifyAdmin(jwt);
        return bookService.updateBook(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes a book from the system by its unique identifier.
     * Restricted to admin users.
     *
     * @param id the unique identifier of the book to be deleted
     * @param jwt the security token of the user attempting the operation
     * @return a ResponseEntity with status 204 No Content on successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        verifyAdmin(jwt);
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
