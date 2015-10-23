package resources;

import entities.ErrorBean;
import entities.Review;
import repositories.BookStubRepository;
import services.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;

@Path("books/{bookId}/reviews")
public class ReviewResource {

    private BookService bookService = BookService.getInstance(BookStubRepository.getInstance());

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllReviews(@PathParam("bookId") String bookId) {
        List<Review> reviews = bookService.getAllReviews(bookId);

        if (reviews == null || reviews.size() == 0)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(new GenericEntity<List<Review>>(reviews) {}).build();
    }

    @GET
    @Path("{reviewId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getReview(@PathParam("bookId") String bookId, @PathParam("reviewId") String reviewId) {
        Review review = bookService.getReviewById(bookId, reviewId);

        if (review == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(review).build();
    }

    @PUT
    @Path("{reviewId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateReview(@PathParam("bookId") String bookId, @PathParam("reviewId") String reviewId, Review review) {
        Review updatedReview = bookService.updateReview(bookId, reviewId, review);

        if(updatedReview == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(updatedReview).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createReview(@PathParam("bookId") String bookId, Review review, @Context UriInfo uriInfo) {
        if (review.getTitle() == null || review.getTitle().length() == 0) {
            ErrorBean error = new ErrorBean();
            error.setErrorCode("validation.missing.title");

            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(error).build();
        }

        Review newReview = bookService.createReview(bookId, review);
        String uri = uriInfo.getBaseUri() + "books/" + bookId + "/reviews/" + newReview.getId();

        return Response.ok().entity(newReview).link(uri, "new_review").build();
    }

    @DELETE
    @Path("{reviewId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteReview(@PathParam("bookId") String bookId, @PathParam("reviewId") String reviewId) {
        boolean reviewWasDeleted = bookService.deleteBookReview(bookId, reviewId);

        if(!reviewWasDeleted)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().build();
    }
}
