package is.hi.hbv501g.bookself.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic entity representing a book on a shelf.
 * Skeleton only - fields/validation will be fleshed out in later hand-ins.
 */
@Entity
@Data
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

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String description;

    @Column
    private String genre; //Maybe an Enum with specific genre types?

    @Column
    private double avgRating;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    //A single book can have many Reviews.
    // mappedBy = book  tells jpa that this is connected to the Book book in review. So JPA doesnt make en extra join table for this but looks inside the Review to see how they are linked.
    //cascage = CascadeType.ALL means that if a new book is added to the database the reviews are added as well. And if the book is deleted the reviews are also deleted.
    private List<Review> reviews = new ArrayList<>();


    public Book(String title, String author, String isbn, String description, String genre){
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
        this.genre = genre;
    }


    //Helper method for BookServiceImpl to calculate new avgrating based on a new review.
    public void calculateAverageRating(){
        if(reviews == null || reviews.isEmpty()){
            this.avgRating = 0.0;
            return;
        }
        double sum = 0.0;
        for(Review review: reviews){
            sum += review.getRating();
        }
        this.avgRating = Math.round((sum / reviews.size()) * 100.0) / 100.0; //Round to two decimal places.
    }
}
