package is.hi.hbv501g.bookself.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a book in the system.
 * Contains book details, average rating and user reviews.
 */
@Entity
@Getter
@Setter
@ToString(exclude = "reviews")
@EqualsAndHashCode(of = "isbn")
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(name = "publication_year", nullable = false)
    private int year;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String description;

    @Column
    private String genre;

    @Column
    private double avgRating;

    /**
     * List of reviews associated with this book.
     * Connected via the 'book' field in the Review entity to avoid extra join tables.
     * If a book is deleted, all its reviews are automatically removed.
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();


    /**
     * Constructor that creates a new book with the specified details.
     * Used when admins add a new book to inventory where the book has no rating or reviews.
     *
     * @param title the title of the book
     * @param author the author of the book
     * @param year the publication year
     * @param isbn the unique ISBN identifier
     * @param description a summary or description of the book
     * @param genre the genre of the book
     */
    public Book(String title, String author, int year, String isbn, String description, String genre){
        this.title = title;
        this.author = author;
        this.year = year;
        this.isbn = isbn;
        this.description = description;
        this.genre = genre;
    }

}
