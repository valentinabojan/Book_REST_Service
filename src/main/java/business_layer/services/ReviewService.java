package business_layer.services;

import business_layer.entity.Review;
import data_access_layer.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("reviewService")
public class ReviewService {

    @Autowired
    private BookRepository bookRepository;

    public List<Review> getAllReviews(Integer bookId) {
        return bookRepository.findAllBookReviews(bookId);
    }

    public Review getReviewById(Integer bookId, Integer reviewId) {
        return bookRepository.findReviewById(bookId, reviewId);
    }

    public boolean deleteBookReview(Integer bookId, Integer reviewId) {
        return bookRepository.deleteBookReview(bookId, reviewId);
    }

    public Review updateReview(Integer bookId, Integer reviewId, Review review) {
        return bookRepository.updateReview(bookId, reviewId, review);
    }

    public Review createReview(Integer bookId, Review review) {
        return bookRepository.createReview(bookId, review);
    }

}
