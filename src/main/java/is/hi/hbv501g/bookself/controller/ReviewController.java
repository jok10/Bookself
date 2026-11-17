package is.hi.hbv501g.bookself.controller;

import is.hi.hbv501g.bookself.dto.ReviewRequest;
import is.hi.hbv501g.bookself.dto.ReviewResponseDTO;
import is.hi.hbv501g.bookself.service.ReviewService;
import is.hi.hbv501g.bookself.service.UserService;
import is.hi.hbv501g.bookself.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;

/**
 * REST controller responsible for managing book reviews.
 * Handles viewing all reviews for a book, paginated review lists,
 * adding/updating reviews, and deleting user reviews.
 */

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    //Get reviews


    /**
     * Retrieves all reviews written for a specific book.
     *
     * @param bookId the unique id of the book
     * @return a list of ReviewResponseDTO objects representing the book's reviews
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByBook(@PathVariable Long bookId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByBook(bookId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Retrieves a paginated list of reviews for a specific book.
     *
     * @param bookId the unique id of the book
     * @param page the page number (zero-indexed, default is 0)
     * @param size the number of reviews per page (default is 3)
     * @return a Page of ReviewResponseDTO objects
     */
    @GetMapping("/book/{bookId}/page")
    public ResponseEntity<Page<ReviewResponseDTO>> getPaginatedReviews(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        Page<ReviewResponseDTO> reviews = reviewService.getPaginatedReviews(bookId, page, size);
        return ResponseEntity.ok(reviews);
    }

    //Review management

    /**
     * Creates a new review or updates an existing review for a book
     * written by the currently authenticated user (one review per user per book).
     *
     * @param request the review request payload containing bookId, rating, and review text
     * @param jwt the security token of the authenticated user
     * @return a 201 Created response containing the saved ReviewResponseDTO
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> addorUpdateReview(
            @Valid @RequestBody ReviewRequest request, @AuthenticationPrincipal Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        User currentUser = userService.getUserById(userId);

        ReviewResponseDTO savedReview = reviewService.addOrUpdateReview(
                currentUser.getId(),
                request.getBookId(),
                request.getRating(),
                request.getReview()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    /**
     * Deletes the authenticated user's review for a specific book.
     *
     * @param bookId the unique identifier of the book whose review is being deleted
     * @param jwt the security token of the authenticated user
     * @return a 204 No Content response on successful deletion
     */
    @DeleteMapping("/book/{bookId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long bookId,
            @AuthenticationPrincipal Jwt jwt) {
        Long userId = ((Number) jwt.getClaim("userId")).longValue();
        reviewService.deleteReview(userId, bookId);
        return ResponseEntity.noContent().build();
    }
}
