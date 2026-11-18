package is.hi.hbv501g.bookself.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an incoming request payload
 * for creating or updating a book in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String title;
    private String author;
    private Integer year;
    private String isbn;
    private String description;
    private String genre;
}
