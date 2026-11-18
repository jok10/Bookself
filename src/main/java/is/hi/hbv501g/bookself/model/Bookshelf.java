package is.hi.hbv501g.bookself.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user's bookshelf that contains a collection of saved books.
 * Each user has a unique one-to-one bookshelf linked via a many-to-many relationship with books.
 */
@Entity
@Getter
@Setter
@ToString(exclude = {"user", "books"})
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor

public class Bookshelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "bookshelf_books",
            joinColumns = @JoinColumn(name = "bookshelf_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books = new ArrayList<>();


    /**
     * Constructor that creates a new bookshelf for the specified user.
     *
     * @param user the user who owns this bookshelf
     */
    public Bookshelf(User user){this.user = user;}

    /**
     * Adds a book to the bookshelf if it is not already present.
     *
     * @param book the book to add
     */
    public void addBook(Book book){
        if (!books.contains(book)){
            books.add(book);
        }
    }

    /**
     * Removes a book from the bookshelf if it exists in the collection.
     *
     * @param book the book to remove
     */
    public void removeBook(Book book){
        if (books.contains(book)){
            books.remove(book);
        }
    }
}