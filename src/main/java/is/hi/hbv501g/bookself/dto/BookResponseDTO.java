package is.hi.hbv501g.bookself.dto;

import is.hi.hbv501g.bookself.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the outgoing response payload
 * for book data sent to clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private int year;
    private String isbn;
    private String description;
    private String genre;
    private double avgRating;

    /**
     * Converts a Book entity into a BookResponseDTO.
     *
     * @const book the Book entity to convert
     * @return a new BookResponseDTO with the book's details
     */
    public static BookResponseDTO fromEntity(Book book){
        return new BookResponseDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getYear(), book.getIsbn(), book.getDescription(), book.getGenre(), book.getAvgRating());
    }
}
