package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.BookRequest;
import is.hi.hbv501g.bookself.dto.BookResponseDTO;
import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.service.BookService;
import is.hi.hbv501g.bookself.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 *REST controller for managing books.
 * Utilizes DTOs for incoming requests and safe client responses.
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
    private void verifyAdmin(HttpSession session) {

        Long currentUserId =
                (Long) session.getAttribute("USER_ID");

        if (currentUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in.");
        }

        User currentUser =
                userService.getUserById(currentUserId);

        if (!currentUser.isAdmin()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Admin access required.");
        }
    }

    @GetMapping
    public List<BookResponseDTO> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(@RequestParam("query") String query){
        return ResponseEntity.ok(bookService.searchBooks(query));
    }

    /**
     * GET /api/books/genre?genre=Fantasy
     * UC10: filter-a catalogue eftir flokki. Ekki case sensitive.
     */
    @GetMapping("/genre")
    public ResponseEntity<List<BookResponseDTO>> getBooksByGenre(@RequestParam("genre") String genre){
        return ResponseEntity.ok(bookService.getBooksByGenre(genre));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequest request, HttpSession session) {
        verifyAdmin(session);
        BookResponseDTO saved = bookService.createBook(request);
        URI location = URI.create("/api/books/" + saved.getId());

        return ResponseEntity.created(location).body(saved);
    }

    /**
     * Updates existing book's information. Only fields provided in the request payload will be updated.
     * @param id the unique identifier of the book.
     * @param request The BookRequest DTO that countains the details to be updated
     * @return A ResponseEntity with status 200 ok and the updated BookResponseDTO or status 404 not found if no book with given ID exists.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @RequestBody BookRequest request, HttpSession session) {
        verifyAdmin(session);
        return bookService.updateBook(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id, HttpSession session) {
        verifyAdmin(session);
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
