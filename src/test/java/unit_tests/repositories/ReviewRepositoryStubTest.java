package unit_tests.repositories;

import business_layer.entity.Author;
import business_layer.entity.Book;
import business_layer.entity.BookCategory;
import business_layer.entity.Review;
import data_access_layer.repositories.BookRepository;
import data_access_layer.repositories.BookRepositoryStub;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewRepositoryStubTest {

    private BookRepository bookRepository;
    private Review review1, review2;
    private static Integer BOOK_ID = 1;

//    @Before
//    public void setUp() {
//        bookRepository = new BookRepositoryStub();
//
//        Author author1 = new Author();
//        author1.setId(1);   author1.setName("Diana Gaba@Transactionallon");
//
//        Book book1 = Book.BookBuilder.book().withTitle("Outlander")
//                .withAuthors(Arrays.asList(author1))
//                .withCategories(Arrays.asList(BookCategory.MYSTERY, BookCategory.DRAMA))
//                .withDate(LocalDate.of(2015, Month.JUNE, 12))
//                .withPrice(17.99)
//                .withIsbn("1-4028-9462-7")
//                .withDescription("A very entertaining book.")
//                .withCoverPath("book1.jpeg")
//                .withPagesNumber(837)
//                .withLanguage("Romanian")
//                .withStars(4.5)
//                .build();
//
//        review1 = Review.ReviewBuilder.review().withTitle("I liked it very much.")
//                .withContent("I liked it very much.")
//                .withUser("Valentina")
//                .withDate(LocalDate.of(2015, Month.OCTOBER, 23))
//                .withBook(book1)
//                .build();
//
//        review2 = Review.ReviewBuilder.review().withTitle("A little dark")
//                .withContent("I found some dark and controversial parts")
//                .withUser("Michaela")
//                .withDate(LocalDate.of(2015, Month.SEPTEMBER, 5))
//                .withBook(book1)
//                .build();
//    }
//
//    @Test
//    public void findReviewById_returnsCorrectReview() {
//        Review review = bookRepository.createReview(BOOK_ID, review1);
//
//        Review foundReview = bookRepository.findReviewById(BOOK_ID, review.getId());
//
//        assertThat(foundReview).isEqualTo(review);
//
//        bookRepository.deleteBookReview(BOOK_ID, review.getId());
//    }
//
//    @Test
//    public void getAllReviews_returnsCorrectListOfReviews() {
//        Review newReview1 = bookRepository.createReview(BOOK_ID, review1);
//        Review newReview2 = bookRepository.createReview(BOOK_ID, review2);
//
//        List<Review> reviews = bookRepository.findAllBookReviews(BOOK_ID);
//
//        assertThat(reviews).isEqualTo(Arrays.asList(newReview1, newReview2));
//
//        bookRepository.deleteBook(newReview1.getId());
//        bookRepository.deleteBook(newReview2.getId());
//    }
//
//    @Test
//    public void createReview_createsNewReview() {
//        Review review = bookRepository.createReview(BOOK_ID, review1);
//
//        assertThat(review.getId()).isNotNull();
//
//        bookRepository.deleteBookReview(BOOK_ID, review.getId());
//    }
//
//    @Test
//    public void updateReview_updatesTheReview() {
//        Review review = bookRepository.createReview(BOOK_ID, review1);
//
//        bookRepository.updateReview(BOOK_ID, review.getId(), review2);
//
//        Review foundReview = bookRepository.findReviewById(BOOK_ID, review.getId());
//        assertThat(foundReview.getTitle()).isEqualTo(review2.getTitle());
//
//        bookRepository.deleteBook(review.getId());
//    }
//
//    @Test
//    public void deleteReview_removesTheCorrectReview() {
//        Review newReview1 = bookRepository.createReview(BOOK_ID, review1);
//        Review newReview2 = bookRepository.createReview(BOOK_ID, review2);
//
//        bookRepository.deleteBookReview(BOOK_ID, newReview1.getId());
//
//        Review foundReview = bookRepository.findReviewById(BOOK_ID, newReview1.getId());
//        assertThat(foundReview).isNull();
//
//        bookRepository.deleteBookReview(BOOK_ID, newReview2.getId());
//    }
}
