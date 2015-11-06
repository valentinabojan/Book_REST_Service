package org.library.business_layer.service;

import org.library.business_layer.entity.Review;
import org.library.data_access_layer.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("reviewService")
@Transactional
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
