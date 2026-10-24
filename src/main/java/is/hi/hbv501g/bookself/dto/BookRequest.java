package is.hi.hbv501g.bookself.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    private String title;
    private String author;
    //private int year;
    private String isbn;
    private String description;
    private String genre;
}
