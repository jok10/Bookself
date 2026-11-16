package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.dto.ReviewResponseDTO;
import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Review;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.repository.BookRepository;
import is.hi.hbv501g.bookself.repository.ReviewRepository;
import is.hi.hbv501g.bookself.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, BookRepository bookRepository, UserRepository userRepository){
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByBook(Long bookId){
        List<Review> reviews = reviewRepository.findByBookId(bookId);

        return reviews.stream().map(ReviewResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponseDTO> getPaginatedReviews(Long bookId, int page, int size) {
        Page<Review> reviewPage = reviewRepository.findByBookId(bookId, PageRequest.of(page, size));
        return reviewPage.map(ReviewResponseDTO::fromEntity);
    }

    @Override
    @Transactional
    public ReviewResponseDTO addOrUpdateReview(Long userId, Long bookId, double rating, String text) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        Optional<Review> existingReview = reviewRepository.findByUserIdAndBookId(userId, bookId);

        Review reviewToSave;
        if (existingReview.isPresent()) {
            reviewToSave = existingReview.get();
            reviewToSave.setRating(rating);
            reviewToSave.setReview(text);
        } else {
            reviewToSave = new Review();
            reviewToSave.setUser(user);
            reviewToSave.setBook(book);
            reviewToSave.setRating(rating);
            reviewToSave.setReview(text);
            reviewToSave.setCreatedAt(LocalDateTime.now());
        }

        Review savedReview = reviewRepository.save(reviewToSave);

        updateBookAverageRating(bookId);
        return ReviewResponseDTO.fromEntity(savedReview);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long requestingUserId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Review not found with ID: " + reviewId));

        User requestingUser = userRepository.findById(requestingUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found."));

        // UC14 only covers deleting your own review - an admin deleting
        // someone else's review is UC15, a separate (not yet built) flow.
        if (!review.isWrittenBy(requestingUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own reviews.");
        }

        Long bookId = review.getBook().getId();
        reviewRepository.delete(review);
        updateBookAverageRating(bookId);
    }

    private void updateBookAverageRating(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        List<Review> reviews = reviewRepository.findByBookId(bookId);

        if (reviews.isEmpty()) {
            book.setAvgRating(0.0);
        } else {
            double sum = 0.0;
            for (Review r : reviews) {
                sum += r.getRating();
            }
            double avg = sum / reviews.size();
            book.setAvgRating(Math.round(avg * 100.0) / 100.0);
        }

        bookRepository.save(book);
    }
}
