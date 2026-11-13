package is.hi.hbv501g.bookself.dto;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Review;
import is.hi.hbv501g.bookself.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private Long userId;
    private String username;
    private String review;
    private double rating;
    private LocalDateTime createdAt;

    public static ReviewResponseDTO fromEntity(Review review) {
        return new ReviewResponseDTO(
                review.getId(),
                review.getBook().getId(),
                review.getBook().getTitle(),
                review.getUser().getId(),
                review.getUser().getUsername(),
                review.getReview(),
                review.getRating(),
                review.getCreatedAt()
        );
    }
}
