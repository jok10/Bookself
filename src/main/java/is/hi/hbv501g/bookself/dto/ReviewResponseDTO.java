package is.hi.hbv501g.bookself.dto;

import is.hi.hbv501g.bookself.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing the outgoing response payload
 * for a book review, including associated book and user details.
 */

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


    /**
     * Converts a Review entity into a ReviewResponseDTO.
     *
     * @param review the Review entity to convert
     * @return a new ReviewResponseDTO with review details, book info, and user info
     */
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
