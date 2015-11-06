package integration_tests;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringLifecycleListener;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.library.application_layer.resource.BookResource;
import org.library.business_layer.entity.Book;
import org.library.business_layer.entity.BookCategory;
import org.library.business_layer.entity.Review;
import org.library.infrastructure.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.filter.RequestContextFilter;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewResourceTest extends JerseyTest {

    private BookServiceClient client;
    private Review review1, review2;
    private Book book;
    private static String PATH;

    @Override
    protected Application configure() {
        ResourceConfig rc = new ResourceConfig();
        forceSet(TestProperties.CONTAINER_PORT, "0");
        rc.register(SpringLifecycleListener.class).register(RequestContextFilter.class);
        rc.registerClasses(BookResource.class);
        rc.property("contextConfig", new AnnotationConfigApplicationContext(SpringConfig.class));
        return rc;
    }

    @Before
    public void setUpTests() {
        client = new BookServiceClient(target());

        book = UtilsTestSetup.createBook1();
        review1 = UtilsTestSetup.createReview1();
        review2 = UtilsTestSetup.createReview2();

        book = client.post("/books", book).readEntity(Book.class);

        PATH = "books" + "/" + book.getId() + "/reviews";
    }

    @After
    public void tearDown() {
        client.deleteAllBooks("/books");
    }

    @Test
    public void givenAReview_GETById_returnsTheCorrectReview() {
        Review review = client.post(PATH, review1).readEntity(Review.class);

        Review foundReview= client.getEntity(PATH + "/" + review.getId()).readEntity(Review.class);

        assertThat(foundReview.getId()).isEqualTo(review.getId());
    }

    @Test
    public void givenManyReviews_GET_returnsTheCorrectListOfReviews() {
        Review newReview1 = client.post(PATH, review1).readEntity(Review.class);
        Review newReview2 = client.post(PATH, review2).readEntity(Review.class);

        List<Review> reviews = client.getAllReviews(PATH).readEntity(new GenericType<List<Review>>() {});

        assertThat(reviews).contains(newReview1);
        assertThat(reviews).contains(newReview2);
    }

    @Test
    public void givenAReview_POST_createsANewReview() {
        Review review = client.post(PATH, review1).readEntity(Review.class);

        assertThat(review.getTitle()).isEqualTo(review1.getTitle());
    }

    @Test
    public void givenAReviewIdAndAReview_PUT_updatesTheReview() {
        Review review = client.post(PATH, review1).readEntity(Review.class);

        client.put(PATH + "/" + review.getId(), review2);

        Review foundReview= client.getEntity(PATH + "/" + review.getId()).readEntity(Review.class);
        assertThat(foundReview.getTitle()).isEqualTo(review2.getTitle());
    }

    @Test
    public void givenAReview_DELETE_deletesTheCorrectReview() {
        Review review = client.post(PATH, review1).readEntity(Review.class);

        client.delete(PATH + "/" + review.getId());

        Response response = client.getEntity(PATH + "/" + review.getId());
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }
}
