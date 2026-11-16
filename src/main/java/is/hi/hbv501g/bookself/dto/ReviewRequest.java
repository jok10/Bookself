package is.hi.hbv501g.bookself.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

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