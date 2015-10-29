package unit_tests.resources;

import business_layer.value_objects.ErrorBean;
import business_layer.entities.Review;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import application_layer.resources.ReviewResource;
import business_layer.services.ReviewService;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

public class ReviewResourceTest {

    private ReviewResource reviewResource;
    private ReviewService mockReviewService;
    private final static String BOOK_ID = "1";
    private final static String REVIEW_ID = "1";

    @Before
    public void setUp() {
        mockReviewService = Mockito.mock(ReviewService.class);
        reviewResource = new ReviewResource();
        reviewResource.setReviewService(mockReviewService);
    }

    @Test
    public void givenAWrongBookIdOrAWrongReviewId_getReviewById_returns404NOTFOUNFD() {
        Mockito.when(mockReviewService.getReviewById(BOOK_ID, REVIEW_ID)).thenReturn(null);

        Response response = reviewResource.getReview(BOOK_ID, REVIEW_ID);

        Mockito.verify(mockReviewService, times(1)).getReviewById(BOOK_ID, REVIEW_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
    }

    @Test
    public void givenAValidBookIdAndAValidReviewId_getReviewById_returns200OK() {
        Review expectedReview = new Review();
        Mockito.when(mockReviewService.getReviewById(BOOK_ID, REVIEW_ID)).thenReturn(expectedReview);

        Response response = reviewResource.getReview(BOOK_ID, REVIEW_ID);

        Mockito.verify(mockReviewService, times(1)).getReviewById(BOOK_ID, REVIEW_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void givenAValidBookIdAndAValidReviewId_getReviewById_returnsCorrectReview() {
        Review expectedReview = new Review();
        Mockito.when(mockReviewService.getReviewById(BOOK_ID, REVIEW_ID)).thenReturn(expectedReview);

        Response response = reviewResource.getReview(BOOK_ID, REVIEW_ID);

        Mockito.verify(mockReviewService, times(1)).getReviewById(BOOK_ID, REVIEW_ID);
        assertThat(response.getEntity()).isEqualTo(expectedReview);
    }

    @Test
    public void givenAValidBookIdAndAValidReviewId_deleteBookReview_returns200OK() {
        Mockito.when(mockReviewService.deleteBookReview(BOOK_ID, REVIEW_ID)).thenReturn(true);

        Response response = reviewResource.deleteReview(BOOK_ID, REVIEW_ID);

        Mockito.verify(mockReviewService, times(1)).deleteBookReview(BOOK_ID, REVIEW_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void givenAWrongBookIdOrAWrongReviewId_deleteBook_returns404NOTFOUND() {
        Mockito.when(mockReviewService.deleteBookReview(BOOK_ID, REVIEW_ID)).thenReturn(false);

        Response response = reviewResource.deleteReview(BOOK_ID, REVIEW_ID);

        Mockito.verify(mockReviewService, times(1)).deleteBookReview(BOOK_ID, REVIEW_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
        assertThat(response.getEntity()).isEqualTo(null);
    }

    @Test
    public void givenAReviewWithoutTitle_createBookReview_returns400BADREQUEST() {
        Review review = new Review();

        Response response = reviewResource.createReview(BOOK_ID, review, null);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
    }

    @Test
    public void givenAReviewWithoutTitle_createBookReview_returnsCorrectErrorMessage() {
        Review review = new Review();

        Response response = reviewResource.createReview(BOOK_ID, review, null);

        assertThat(((ErrorBean)response.getEntity()).getErrorCode()).isEqualTo("validation.missing.title");
    }

    @Test
    public void givenAValidReview_createBookReview_returns200OK() {
        Review review = new Review();
        review.setTitle("title");
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockReviewService.createReview(BOOK_ID, review)).thenReturn(review);

        Response response = reviewResource.createReview(BOOK_ID, review, mockUriInfo);

        Mockito.verify(mockReviewService, times(1)).createReview(BOOK_ID, review);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void givenAValidBook_createBook_returnsCorrectBook() {
        Review review = new Review();
        review.setTitle("title");
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockReviewService.createReview(BOOK_ID, review)).thenReturn(review);

        Response response = reviewResource.createReview(BOOK_ID, review, mockUriInfo);

        Mockito.verify(mockReviewService, times(1)).createReview(BOOK_ID, review);
        assertThat(response.getEntity()).isEqualTo(review);
    }

    @Test
    public void givenAValidBook_createBook_returnsLinkToTheNewBook() {
        Review review = new Review();
        review.setTitle("title");
        review.setId("1");
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockReviewService.createReview(BOOK_ID, review)).thenReturn(review);
        Mockito.when(mockUriInfo.getBaseUri()).thenReturn(URI.create("http://localhost:8080/webapi/"));

        Response response = reviewResource.createReview(BOOK_ID, review, mockUriInfo);

        Mockito.verify(mockReviewService, times(1)).createReview(BOOK_ID, review);
        Mockito.verify(mockUriInfo, times(1)).getBaseUri();
        assertThat(response.getLink("new_review").getUri()).isEqualTo(URI.create("http://localhost:8080/webapi/books/1/reviews/1"));
    }

    @Test
    public void givenAWrongBookIdOrReviewId_updateBookReview_returns404NOTFOUND() {
        Review review = new Review();
        Mockito.when(mockReviewService.updateReview(BOOK_ID, REVIEW_ID, review)).thenReturn(null);

        Response response = reviewResource.updateReview(BOOK_ID, REVIEW_ID, review);

        Mockito.verify(mockReviewService, times(1)).updateReview(BOOK_ID, REVIEW_ID, review);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
        assertThat(response.getEntity()).isEqualTo(null);
    }

    @Test
    public void givenAValidBookIdAndAValidReviewIdAndABook_updateBookReview_returns200OK() {
        Review review = new Review();
        Mockito.when(mockReviewService.updateReview(BOOK_ID, REVIEW_ID, review)).thenReturn(review);

        Response response = reviewResource.updateReview(BOOK_ID, REVIEW_ID, review);

        Mockito.verify(mockReviewService, times(1)).updateReview(BOOK_ID, REVIEW_ID, review);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void givenAValidBookIdAndAValidReviewIdAndABook_updateBookReview_returnsUpdatedBook() {
        Review review = new Review();
        Mockito.when(mockReviewService.updateReview(BOOK_ID, REVIEW_ID, review)).thenReturn(review);

        Response response = reviewResource.updateReview(BOOK_ID, REVIEW_ID, review);

        Mockito.verify(mockReviewService, times(1)).updateReview(BOOK_ID, REVIEW_ID, review);
        assertThat(response.getEntity()).isEqualTo(review);
    }

    @Test
    public void givenABookIdAndNoReviewsForThatBookInDB_getAllBookReview_returns404NOTFOUND() {
        Mockito.when(mockReviewService.getAllReviews(BOOK_ID)).thenReturn(null);

        Response response = reviewResource.getAllReviews(BOOK_ID);

        Mockito.verify(mockReviewService, times(1)).getAllReviews(BOOK_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
    }

    @Test
    public void givenABookId_getAllBookReview_returns200OK() {
        List<Review> reviews = Arrays.asList(new Review());
        Mockito.when(mockReviewService.getAllReviews(BOOK_ID)).thenReturn(reviews);

        Response response = reviewResource.getAllReviews(BOOK_ID);

        Mockito.verify(mockReviewService, times(1)).getAllReviews(BOOK_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void givenABookId_getAllBookReview_returnsCorrectBooksList() {
        List<Review> reviews = Arrays.asList(new Review());
        Mockito.when(mockReviewService.getAllReviews(BOOK_ID)).thenReturn(reviews);

        Response response = reviewResource.getAllReviews(BOOK_ID);

        Mockito.verify(mockReviewService, times(1)).getAllReviews(BOOK_ID);
        assertThat(response.getEntity()).isEqualTo(reviews);
    }
}
