package services;

import entities.Book;
import entities.Review;
import repositories.BookRepository;

import java.io.File;
import java.util.List;

public class BookService {

    private static BookService bookServiceInstance;

    private BookRepository bookRepository;

    private BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public static BookService getInstance(BookRepository bookRepository) {
        if (bookServiceInstance == null)
            bookServiceInstance = new BookService(bookRepository);
        return bookServiceInstance;
    }

    public List<Book> getAllBooks(String start, String end, String author, String title, String price, String sortCriteria) {
        return bookRepository.getAllBooksWithPaginationAndFilteringAndSorting(start, end, author, title, price, sortCriteria);
    }

    public Book getBook(String bookId) {
        return bookRepository.findBookById(bookId);
    }

    public boolean deleteBook(String bookId) {
        return bookRepository.deleteBook(bookId);
    }

    public int getBooksCount() {
        return bookRepository.findAllBooks().size();
    }

    public Book createBook(Book book) {
        return bookRepository.createBook(book);
    }

    public Book updateBook(String bookId, Book book) {
        return bookRepository.updateBook(bookId, book);
    }


    public File getBookCover(String bookId) {
        return bookRepository.findBookCover(bookId);
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
