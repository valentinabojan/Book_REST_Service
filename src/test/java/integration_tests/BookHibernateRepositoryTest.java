package integration_tests;

import business_layer.entity.Author;
import business_layer.entity.Book;
import business_layer.entity.BookCategory;
import business_layer.entity.Review;
import data_access_layer.repositories.BookRepository;
import infrastructure.SpringConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class)
@Transactional
public class BookHibernateRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    private Book book1, book2, book3, book4;
    private Review review1, review2;

    @Before
    public void setUp() {
        String author1 = "Diana Gabalon";
        String author2 = "Erich Gamma";
        String author3 = "Richard Helm";
        String author4 = "John Vlissides";

        List<String> authors1 = new ArrayList<>();
        authors1.add(author1);
        List<String> authors2 = new ArrayList<>();
        authors2.add(author2);
        authors2.add(author3);
        authors2.add(author4);

        List<BookCategory> categories1 = new ArrayList<>();
        categories1.add(BookCategory.MYSTERY);
        categories1.add(BookCategory.DRAMA);
        List<BookCategory> categories2 = new ArrayList<>();
        categories2.add(BookCategory.SCIENCE);

        book1 = Book.BookBuilder.book().withTitle("Outlander")
                .withAuthors(authors1)
                .withCategories(categories1)
                .withDate(LocalDate.of(2015, Month.JUNE, 12))
                .withPrice(17.99)
                .withIsbn("1-4028-9462-7")
                .withDescription("A very entertaining book.")
                .withCoverPath("book1.jpeg")
                .withPagesNumber(837)
                .withLanguage("Romanian")
                .withStars(4.5)
                .build();

        book2 = Book.BookBuilder.book().withTitle("Design Patterns")
                .withAuthors(authors2)
                .withCategories(categories2)
                .withDate(LocalDate.of(2012, Month.MARCH, 1))
                .withPrice(59.99)
                .withIsbn("0-201-63361-2")
                .withDescription("Design patterns for everyone.")
                .withCoverPath("book2.jpeg")
                .withPagesNumber(395)
                .withLanguage("English")
                .withStars(5)
                .build();

        book3 = Book.BookBuilder.book().withTitle("Design Patterns")
                .withAuthors(authors2)
                .withPrice(99.99)
                .withDate(LocalDate.of(2012, Month.MARCH, 2))
                .withIsbn("0-201-63361-0")
                .build();

        book4 = Book.BookBuilder.book().withTitle("Design Patterns")
                .withAuthors(new ArrayList<>())
                .withIsbn("0-201-63361-8")
                .withPrice(59.99)
                .withDate(LocalDate.of(2010, Month.MARCH, 3))
                .build();

        review1 = Review.ReviewBuilder.review().withTitle("I liked it very much.")
                .withContent("I liked it very much.")
                .withUser("Valentina")
                .withDate(LocalDate.of(2015, Month.OCTOBER, 23))
                .build();

        review2 = Review.ReviewBuilder.review().withTitle("A little dark")
                .withContent("I found some dark and controversial parts")
                .withUser("Michaela")
                .withDate(LocalDate.of(2015, Month.SEPTEMBER, 5))
                .build();
    }

    @Test
    public void countBooks_returnsCorrectSize() {
        bookRepository.createBook(book1);

        long booksCount = bookRepository.getBooksCount();

        assertThat(booksCount).isEqualTo(1);
    }

    @Test
    public void findBookById_returnsCorrectBook() {
        Book book = bookRepository.createBook(book1);

        Book foundBook = bookRepository.findBookById(book.getId());

        assertThat(foundBook).isEqualTo(book);
    }

    @Test
    public void givenNoParameters_getAllBooks_returnsCorrectListOfBooks() {
        Book newBook1 = bookRepository.createBook(book1);
        Book newBook2 = bookRepository.createBook(book2);

        List<Book> books = bookRepository.findAllBooksWithPaginationAndFilteringAndSorting(null, null, null, null, null, null);

        assertThat(books).contains(newBook1);
        assertThat(books).contains(newBook2);
    }

    @Test
    public void givenPaginationParameters_getAllBooks_returnsCorrectListOfBooks() {
        Book newBook1 = bookRepository.createBook(book1);
        Book newBook2 = bookRepository.createBook(book2);
        Book newBook3 = bookRepository.createBook(book3);

        List<Book> books = bookRepository.findAllBooksWithPaginationAndFilteringAndSorting("0", "1", null, null, null, null);

        assertThat(books).contains(newBook1);
        assertThat(books).contains(newBook2);
    }

    @Test
    public void givenFilteringParameters_getAllBooks_returnsCorrectListOfBooks() {
        Book newBook1 = bookRepository.createBook(book1);
        Book newBook2 = bookRepository.createBook(book2);
        Book newBook3 = bookRepository.createBook(book3);
        Book newBook4 = bookRepository.createBook(book4);

        List<Book> books = bookRepository.findAllBooksWithPaginationAndFilteringAndSorting(null, null, "Erich Gamma", "Design Patterns", "0,200", null);

        assertThat(books).contains(newBook2);
        assertThat(books).contains(newBook3);
    }

    @Test
    public void givenSortingParameters_getAllBooks_returnsCorrectListOfBooks() {
        Book newBook1 = bookRepository.createBook(book1);
        Book newBook2 = bookRepository.createBook(book2);
        Book newBook3 = bookRepository.createBook(book3);
        Book newBook4 = bookRepository.createBook(book4);

        List<Book> books = bookRepository.findAllBooksWithPaginationAndFilteringAndSorting(null, null, null, null, null, "title,author,price,year");

        assertThat(books).isEqualTo(Arrays.asList(newBook4, newBook2, newBook3, newBook1));
    }

    @Test
    public void createBook_createsNewBook() {
        Book book = bookRepository.createBook(book1);

        long booksCount = bookRepository.getBooksCount();
        Book foundBook = bookRepository.findBookById(book.getId());
        assertThat(booksCount).isEqualTo(1);
        assertThat(foundBook.getTitle()).isEqualTo(book1.getTitle());
    }

    @Test
    public void updateBook_updatesTheBook() {
        Book book = bookRepository.createBook(book1);

        bookRepository.updateBook(book.getId(), book2);

        Book foundBook = bookRepository.findBookById(book.getId());
        assertThat(foundBook.getTitle()).isEqualTo(book2.getTitle());
    }

    @Test
    public void deleteBook_removesTheCorrectBook() {
        Book newBook1 = bookRepository.createBook(book1);
        bookRepository.createBook(book2);

        bookRepository.deleteBook(newBook1.getId());

        long booksCount = bookRepository.getBooksCount();
        assertThat(booksCount).isEqualTo(1);
    }

    @Test
    public void findReviewById_returnsCorrectReview() {
        Book book = bookRepository.createBook(book1);
        Review review = bookRepository.createReview(book.getId(), review1);

        Review foundReview = bookRepository.findReviewById(book.getId(), review.getId());

        assertThat(foundReview).isEqualTo(review);
    }

    @Test
    public void getAllReviews_returnsCorrectListOfReviews() {
        Book book = bookRepository.createBook(book1);
        Review newReview1 = bookRepository.createReview(book.getId(), review1);
        Review newReview2 = bookRepository.createReview(book.getId(), review2);

        List<Review> reviews = bookRepository.findAllBookReviews(book.getId());

        assertThat(reviews).contains(newReview1);
        assertThat(reviews).contains(newReview2);
    }

    @Test
    public void createReview_createsNewReview() {
        Book book = bookRepository.createBook(book1);

        Review review = bookRepository.createReview(book.getId(), review1);

        assertThat(review.getId()).isNotNull();
    }

    @Test
    public void updateReview_updatesTheReview() {
        Book book = bookRepository.createBook(book1);
        Review review = bookRepository.createReview(book.getId(), review1);

        bookRepository.updateReview(book.getId(), review.getId(), review2);

        Review foundReview = bookRepository.findReviewById(book.getId(), review.getId());
        assertThat(foundReview.getTitle()).isEqualTo(review2.getTitle());
    }

    @Test
    public void deleteReview_removesTheCorrectReview() {
        Book book = bookRepository.createBook(book1);
        Review newReview1 = bookRepository.createReview(book.getId(), review1);
        Review newReview2 = bookRepository.createReview(book.getId(), review2);

        bookRepository.deleteBookReview(book.getId(), newReview1.getId());

        Review foundReview = bookRepository.findReviewById(book.getId(), newReview1.getId());
        assertThat(foundReview).isNull();
    }
}

