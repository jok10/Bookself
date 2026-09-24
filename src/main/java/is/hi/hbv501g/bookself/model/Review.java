package is.hi.hbv501g.bookself.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000) //Decide what the limit should be.
    private String review;

    @Column(nullable = false)
    private double rating;

    @ManyToOne(fetch = FetchType.LAZY) //Many instances of this class point to a single instace of book. This side is the owner of the relationship.
    @JoinColumn(name = "book_id", nullable = false) //Book_id is the name of the column in the database.
    @JsonIgnore // tells JSON converter to skip this field when converting Review object into JSON. It prevents an infinite loop that would crash the system.
    private Book book;

}
