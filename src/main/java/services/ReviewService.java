package services;

import entities.Review;
import repositories.BookRepository;

import java.util.List;

public class ReviewService {

    private static ReviewService reviewServiceInstance;

    private BookRepository bookRepository;

    private ReviewService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public static ReviewService getInstance(BookRepository bookRepository) {
        if (reviewServiceInstance == null)
            reviewServiceInstance = new ReviewService(bookRepository);
        return reviewServiceInstance;
    }

    public List<Review> getAllReviews(String bookId) {
        return bookRepository.findAllBookReviews(bookId);
    }

    public Review getReviewById(String bookId, String reviewId) {
        return bookRepository.findReviewById(bookId, reviewId);
    }

    public boolean deleteBookReview(String bookId, String reviewId) {
        return bookRepository.deleteBookReview(bookId, reviewId);
    }

    public Review updateReview(String bookId, String reviewId, Review review) {
        return bookRepository.updateReview(bookId, reviewId, review);
    }

    public Review createReview(String bookId, Review review) {
        return bookRepository.createReview(bookId, review);
    }

}
