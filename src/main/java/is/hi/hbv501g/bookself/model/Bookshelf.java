package is.hi.hbv501g.bookself.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    public Bookshelf(User user){this.user = user;}

    public void addBook(Book book){
        if (!books.contains(book)){
            books.add(book);
        }
    }

    public void removeBook(Book book){
        if (books.contains(book)){
            books.remove(book);
        }
    }
}