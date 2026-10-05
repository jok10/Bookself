package is.hi.hbv501g.bookself.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private Long userId;
    private Long bookId;
    private double rating;
    private String review;
}