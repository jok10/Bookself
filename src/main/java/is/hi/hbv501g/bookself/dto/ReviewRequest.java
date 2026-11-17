package is.hi.hbv501g.bookself.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object (DTO) representing an incoming request payload
 * for creating or updating a book review. Includes validation and boundaries
 * for rating (0.0 to 5.0) and text length (1000 characters).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    @NotNull (message = "Book ID is required")
    private Long bookId;

    @DecimalMin(value = "0.0", message = "Rating cannot be less than 0")
    @DecimalMax(value = "5.0", message = "Rating cannot be more than 5")
    private double rating;

    @Size(max = 1000, message = "Review cannot exceed 1000 characters")
    private String review;
}