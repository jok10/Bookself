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

/**
 * Implementation of the ReviewService interface for managing book reviews, ratings, and score calculations.
 */
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with ID: " + bookId));

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

    private void updateBookAverageRating(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with ID: " + bookId));

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
    @Override
    public void deleteReview (Long userId, Long bookId){
        Review review = reviewRepository.findByUserIdAndBookId(userId, bookId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found for this book"));
        reviewRepository.delete(review);
        updateBookAverageRating(bookId);
    }
}
