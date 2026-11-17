package is.hi.hbv501g.bookself.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


/**
 * Represents a review and rating written by a user for a specific book.
 * Manages relationships with both the reviewed book and the author of the review.
 */
@Entity
@Getter
@Setter
@ToString(exclude = {"book", "user"}) // Exclude relationships to prevent toString loops
@EqualsAndHashCode(of = "id") // Anchor equals/hashCode to the ID
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String review;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * The book being reviewed.
     * Many instances of this class point to a single instance of book
     * Marked with LAZY fetch to optimize performance
     * and @JsonIgnore to prevent infinite JSON loops.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    @JsonIgnore
    private Book book;

    /**
     * The user who wrote the review.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;
}
