package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.ReviewRequest;
import is.hi.hbv501g.bookself.model.Review;
import is.hi.hbv501g.bookself.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    /**GET /api/reviews/book/{bookId}
     * Retrieves all reviews of a given book
     */

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getReviewsByBook(@PathVariable Long bookId){
        List<Review> reviews = reviewService.getReviewsByBook(bookId);
        return ResponseEntity.ok(reviews);
    }

    //Later: get paginated reviews

    /**
     * POST /api/reviews
     * Creates a new review or updates an existing review.
     */
    @PostMapping
    public ResponseEntity<Review> addorUpdateReview(@RequestBody ReviewRequest request){
        Review savedReview = reviewService.addOrUpdateReview(
                request.getUserId(),
                request.getBookId(),
                request.getRating(),
                request.getReview()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }
}
