package org.library.data_access_layer.repository;

import org.library.business_layer.entity.Book;
import org.library.business_layer.entity.Review;
import org.library.business_layer.value_object.BookList;

import java.io.File;
import java.util.List;

public interface BookRepository {

    Book findBookById(Integer bookId);

    boolean deleteBook(Integer bookId);

    Book createBook(Book book);

    Book updateBook(Integer bookId, Book book);

    BookList findAllBooksWithPaginationAndFilteringAndSorting(String start, String end, String author, String title, String price, String sortCriteria);

    File findBookCover(Integer bookId);

    List<Review> findAllBookReviews(Integer bookId);

    Review findReviewById(Integer bookId, Integer reviewId);

    boolean deleteBookReview(Integer bookId, Integer reviewId);

    Review updateReview(Integer bookId, Integer reviewId, Review review);

    Review createReview(Integer bookId, Review review);

    Long getBooksCount();
}
