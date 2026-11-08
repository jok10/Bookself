package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.BookRequest;
import is.hi.hbv501g.bookself.dto.BookResponseDTO;
import is.hi.hbv501g.bookself.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 *REST controller for managing books.
 * Utilizes DTOs for incoming requests and safe client responses.
 * Create/update/delete are admin-only, enforced by @PreAuthorize below -
 * SecurityConfig's filter chain already guarantees anything that isn't a
 * GET on this path has a valid, authenticated caller before we even get
 * here; @PreAuthorize narrows that further to admins specifically.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
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


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequest request) {
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
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @RequestBody BookRequest request) {
        return bookService.updateBook(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
