package integration_tests;

import application_layer.BookResource;
import application_layer.ReviewResource;
import business_layer.entity.Author;
import business_layer.entity.Book;
import business_layer.entity.BookCategory;
import business_layer.entity.Review;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringLifecycleListener;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.filter.RequestContextFilter;
import infrastructure.SpringConfig;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewResourceTest extends JerseyTest {

    private BookServiceClient client;
    private Review review1, review2;
    private static String BOOK_ID = "1";
    private static String MAIN_PATH = "books" + "/" + BOOK_ID + "/reviews";
    private Book book1;

    @Override
    protected Application configure() {
        ResourceConfig rc = new ResourceConfig();
        forceSet(TestProperties.CONTAINER_PORT, "0");
        rc.register(SpringLifecycleListener.class).register(RequestContextFilter.class);
        rc.registerClasses(BookResource.class);
        rc.registerClasses(ReviewResource.class);
        rc.property("contextConfig", new AnnotationConfigApplicationContext(SpringConfig.class));
        return rc;
    }

    @Before
    public void setUpTests() {
        client = new BookServiceClient(target());

        String author1 = "Diana Gabalon";
        List<String> authors1 = new ArrayList<>();
        authors1.add(author1);

        List<BookCategory> categories1 = new ArrayList<>();
        categories1.add(BookCategory.MYSTERY);
        categories1.add(BookCategory.DRAMA);

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

    @After
    public void tearDown() {
        client.deleteAllBooks("/books");
    }

    @Test
    public void givenAReview_GETById_returnsTheCorrectReview() {
        Book book = client.post("/books", book1).readEntity(Book.class);

        Review review = client.post("books" + "/" + book.getId() + "/reviews", review1).readEntity(Review.class);

        Review foundReview= client.getEntity("books" + "/" + book.getId() + "/reviews" + "/" + review.getId()).readEntity(Review.class);

        assertThat(foundReview.getId()).isEqualTo(review.getId());
    }

    @Test
    public void givenManyReviews_GET_returnsTheCorrectListOfReviews() {
        Review newReview1 = client.post(MAIN_PATH, review1).readEntity(Review.class);
        Review newReview2 = client.post(MAIN_PATH, review2).readEntity(Review.class);

        List<Review> reviews = client.getAllReviews(MAIN_PATH).readEntity(new GenericType<List<Review>>() {});

        assertThat(reviews.get(0).getId()).isEqualTo(newReview1.getId());
        assertThat(reviews.get(1).getId()).isEqualTo(newReview2.getId());
    }

    @Test
    public void givenAReview_POST_createsANewReview() {
        Review review = client.post(MAIN_PATH, review1).readEntity(Review.class);

        assertThat(review.getTitle()).isEqualTo(review1.getTitle());
    }

    @Test
    public void givenAReviewIdAndAReview_PUT_updatesTheReview() {
        Review review = client.post(MAIN_PATH, review1).readEntity(Review.class);

        client.put(MAIN_PATH + "/" + review.getId(), review2);

        Review foundReview= client.getEntity(MAIN_PATH + "/" + review.getId()).readEntity(Review.class);
        assertThat(foundReview.getTitle()).isEqualTo(review2.getTitle());
    }

    @Test
    public void givenAReview_DELETE_deletesTheCorrectReview() {
        Review review = client.post(MAIN_PATH, review1).readEntity(Review.class);

        client.delete(MAIN_PATH + "/" + review.getId());

        Response response = client.getEntity(MAIN_PATH + "/" + review.getId());
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }
}
