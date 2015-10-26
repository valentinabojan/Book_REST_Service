package repositories;

import entities.Book;
import entities.Review;

import java.io.File;
import java.util.List;

public interface BookRepository {

    Book findBookById(String bookId);

    boolean deleteBook(String bookId);

    Book createBook(Book book);

    Book updateBook(String bookId, Book book);

    List<Book> findAllBooksWithPaginationAndFilteringAndSorting(String start, String end, String author, String title, String price, String sortCriteria);

    File findBookCover(String bookId);

    List<Review> findAllBookReviews(String bookId);

    Review findReviewById(String bookId, String reviewId);

    boolean deleteBookReview(String bookId, String reviewId);

    Review updateReview(String bookId, String reviewId, Review review);

    Review createReview(String bookId, Review review);

    int getBooksCount();
}
