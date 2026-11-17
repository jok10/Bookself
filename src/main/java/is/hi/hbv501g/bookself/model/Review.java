package is.hi.hbv501g.bookself.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY) //Many instances of this class point to a single instance of book. This side is the owner of the relationship.
    @JoinColumn(name = "book_id", nullable = false) //Book_id is the name of the column in the database.
    @JsonIgnore // tells JSON converter to skip this field when converting Review object into JSON. It prevents an infinite loop that would crash the system.
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    //Helper method to check if agiven user owns this review. For permission cheks when deleting or editing review.
    public boolean isWrittenBy(User user){
        if (user == null || this.user == null) {
            return false;
        }
        if (this.user.getId() != null && user.getId() != null) {
            return this.user.getId().equals(user.getId());
        }
        return this.user.equals(user);
    }
}
