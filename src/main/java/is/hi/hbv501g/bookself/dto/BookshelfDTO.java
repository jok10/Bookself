package is.hi.hbv501g.bookself.dto;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Bookshelf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) used to safely expose bookshelf data.
 * Prevents leaking internal user information such as passwords and
 * unnecessary entity relationships.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookshelfDTO {

    private Long bookshelfId;
    private Long userId;
    private List<Book> books;

    /**
     * Converts a Bookshelf entity into a BookshelfDTO.
     *
     * @param bookshelf the Bookshelf entity to convert
     * @return a BookshelfDTO containing safe bookshelf information
     */
    public static BookshelfDTO fromEntity(Bookshelf bookshelf) {
        return new BookshelfDTO(
                bookshelf.getId(),
                bookshelf.getUser().getId(),
                bookshelf.getBooks()
        );
    }
}