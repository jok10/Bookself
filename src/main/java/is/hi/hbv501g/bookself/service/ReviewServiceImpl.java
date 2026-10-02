package is.hi.hbv501g.bookself.service;

import is.hi.hbv501g.bookself.model.Book;
import is.hi.hbv501g.bookself.model.Review;
import is.hi.hbv501g.bookself.model.User;
import is.hi.hbv501g.bookself.repository.BookRepository;
import is.hi.hbv501g.bookself.repository.ReviewRepository;
import is.hi.hbv501g.bookself.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public List<Review> getReviewsByBook(Long bookId){
        return reviewRepository.findByBookId(bookId);
    }

    @Override
    public Review addOrUpdateReview(Long userId, Long bookId, int rating, String text) {
        Review reviewToSave;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        Optional<Review> existingReview = reviewRepository.findByUserIdAndBookId(userId, bookId);

        if(existingReview.isPresent()){
            reviewToSave = existingReview.get();
            reviewToSave.setRating(rating);
            reviewToSave.setReview(text);
        }else{
            reviewToSave = new Review();
            reviewToSave.setUser(user);
            reviewToSave.setBook(book);
            reviewToSave.setRating(rating);
            reviewToSave.setCreatedAt(LocalDateTime.now());
        }

        Review savedReview = reviewRepository.save(reviewToSave);

        updateBookAverageRating(book);
        return savedReview;
    }

    private void updateBookAverageRating(Book book) {
        // Re-fetch reviews or ensure book.getReviews() is populated
        List<Review> reviews = reviewRepository.findByBookId(book.getId());
        book.setReviews(reviews);

        // Call the domain method on the Book entity
        book.calculateAverageRating();

        // Persist the updated average rating
        bookRepository.save(book);
    }
}
