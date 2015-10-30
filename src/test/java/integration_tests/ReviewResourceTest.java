package integration_tests;

import application_layer.resources.ReviewResource;
import business_layer.entities.Review;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringLifecycleListener;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.filter.RequestContextFilter;
import spring.AppConfig;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReviewResourceTest extends JerseyTest {

    private BookServiceClient client;
    private Review review1, review2;
    private static String BOOK_ID = "1";
    private static String MAIN_PATH = "books" + "/" + BOOK_ID + "/reviews";

    @Override
    protected Application configure() {
        ResourceConfig rc = new ResourceConfig();
        forceSet(TestProperties.CONTAINER_PORT, "0");
        rc.register(SpringLifecycleListener.class).register(RequestContextFilter.class);
        rc.registerClasses(ReviewResource.class);
        rc.property("contextConfig", new AnnotationConfigApplicationContext(AppConfig.class));
        return rc;
    }

    @Before
    public void setUpTests() {
        client = new BookServiceClient(target());

        review1 = Review.ReviewBuilder.review().withTitle("I liked it very much.")
                                                .withContent("I liked it very much.")
                                                .withUser("Valentina")
                                                .withDate(LocalDate.of(2015, Month.OCTOBER, 23))
                                                .withBookId("1")
                                                .build();

        review2 = Review.ReviewBuilder.review().withTitle("I liked it very much.")
                                                .withContent("I found some dark and controversial parts")
                                                .withUser("Michaela")
                                                .withDate(LocalDate.of(2015, Month.SEPTEMBER, 5))
                                                .withBookId("1")
                                                .build();
    }

    @After
    public void tearDown() {
        client.deleteAllReviews(MAIN_PATH);
    }

    @Test
    public void givenAReview_GETById_returnsTheCorrectReview() {
        Review review = client.post(MAIN_PATH, review1).readEntity(Review.class);

        Review foundReview= client.getEntity(MAIN_PATH + "/" + review.getId()).readEntity(Review.class);

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
