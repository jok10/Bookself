package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.ReviewRequest;
import is.hi.hbv501g.bookself.model.Review;
import is.hi.hbv501g.bookself.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * GET /api/reviews/book/{bookId}
     * Retrieves all reviews of a given book
     */

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getReviewsByBook(@PathVariable Long bookId) {
        List<Review> reviews = reviewService.getReviewsByBook(bookId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * GET /api/reviews/book/{bookId}/page?page=0&size=3
     * UC20: returns reviews for a book in pages instead of all at once.
     * page is zero-indexed, size is how many reviews per page.
     */
    @GetMapping("/book/{bookId}/page")
    public ResponseEntity<Page<Review>> getPaginatedReviews(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Page<Review> reviews = reviewService.getPaginatedReviews(bookId, page, size);
        return ResponseEntity.ok(reviews);
    }

    /**
     * POST /api/reviews
     * Creates a new review or updates an existing review.
     */
    @PostMapping
    public ResponseEntity<Review> addorUpdateReview(@RequestBody ReviewRequest request, HttpSession session) {
        Long currentUserId = (Long) session.getAttribute("USER_ID");
        if (currentUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "You must be logged in to write reviews.");
        }
        Review savedReview = reviewService.addOrUpdateReview(
                currentUserId,
                request.getBookId(),
                request.getRating(),
                request.getReview()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }
}
