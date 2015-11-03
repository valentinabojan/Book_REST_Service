package application_layer;

import business_layer.entity.Review;
import business_layer.services.ReviewService;
import business_layer.value_objects.ErrorBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("books/{bookId}/reviews")
@Component
public class ReviewResource {

    @Autowired
    private ReviewService reviewService;

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllReviews(@PathParam("bookId") Integer bookId) {
        List<Review> reviews = reviewService.getAllReviews(bookId);

        if (reviews == null || reviews.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(new GenericEntity<List<Review>>(reviews) {}).build();
    }

    @GET
    @Path("{reviewId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getReview(@PathParam("bookId") Integer bookId, @PathParam("reviewId") Integer reviewId) {
        Review review = reviewService.getReviewById(bookId, reviewId);

        if (review == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(review).build();
    }

    @PUT
    @Path("{reviewId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateReview(@PathParam("bookId") Integer bookId, @PathParam("reviewId") Integer reviewId, Review review) {
        Review updatedReview = reviewService.updateReview(bookId, reviewId, review);

        if(updatedReview == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(updatedReview).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createReview(@PathParam("bookId") Integer bookId, Review review, @Context UriInfo uriInfo) {
        if (review.getTitle() == null || review.getTitle().length() == 0) {
            ErrorBean error = new ErrorBean();
            error.setErrorCode("validation.missing.title");

            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(error).build();
        }

        Review newReview = reviewService.createReview(bookId, review);
        String uri = uriInfo.getBaseUri() + "books/" + bookId + "/reviews/" + newReview.getId();

        return Response.ok().entity(newReview).link(uri, "new_review").build();
    }

    @DELETE
    @Path("{reviewId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteReview(@PathParam("bookId") Integer bookId, @PathParam("reviewId") Integer reviewId) {
        boolean reviewWasDeleted = reviewService.deleteBookReview(bookId, reviewId);

        if(!reviewWasDeleted)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().build();
    }
}
