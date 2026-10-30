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
        return bookService.findAll().stream().map(BookResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(book -> ResponseEntity.ok(BookResponseDTO.fromEntity(book)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(@RequestParam("query") String query){
        List<BookResponseDTO> results = bookService.searchBooks(query).stream().map(BookResponseDTO::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    /**
     * GET /api/books/genre?genre=Fantasy
     * UC10: filter-a catalogue eftir flokki. Ekki case sensitive.
     */
    @GetMapping("/genre")
    public ResponseEntity<List<BookResponseDTO>> getBooksByGenre(@RequestParam("genre") String genre){
        List<BookResponseDTO> results = bookService.getBooksByGenre(genre).stream().map(BookResponseDTO::fromEntity).collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequest request, HttpSession session) {
        verifyAdmin(session);
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setYear(request.getYear());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setGenre(request.getGenre());

        Book saved = bookService.save(book);
        URI location = URI.create("/api/books/" + saved.getId());

        return ResponseEntity.created(location).body(BookResponseDTO.fromEntity(saved));
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
        return bookService.findById(id)
                .map(existing -> {
            // Only update fields if the client actually provided a new value
            if (request.getTitle() != null && !request.getTitle().isBlank()) {
                existing.setTitle(request.getTitle());
            }
            if (request.getAuthor() != null && !request.getAuthor().isBlank()) {
                existing.setAuthor(request.getAuthor());
            }
            if (request.getYear() > 0) {
                existing.setYear(request.getYear());
            }
            if (request.getIsbn() != null && !request.getIsbn().isBlank()) {
                existing.setIsbn(request.getIsbn());
            }
            if (request.getDescription() != null && !request.getDescription().isBlank()) {
                existing.setDescription(request.getDescription());
            }
            if (request.getGenre() != null && !request.getGenre().isBlank()) {
                existing.setGenre(request.getGenre());
            }

            Book updated = bookService.save(existing);
            return ResponseEntity.ok(BookResponseDTO.fromEntity(updated));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id, HttpSession session) {
        verifyAdmin(session);
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
