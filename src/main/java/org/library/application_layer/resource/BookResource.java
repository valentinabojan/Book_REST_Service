package org.library.application_layer.resource;

import org.library.business_layer.entity.Book;
import org.library.business_layer.entity.Review;
import org.library.business_layer.service.BookService;
import org.library.business_layer.service.ReviewService;
import org.library.business_layer.value_object.ErrorBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.net.URI;
import java.util.List;

@Path("/books")
@Component
public class BookResource {

    @Autowired
    private BookService bookService;

    @Autowired
    private ReviewService reviewService;

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllBooks(@QueryParam(value="start") String start,
                                @QueryParam(value="end") String end,
                                @QueryParam(value="author") String author,
                                @QueryParam(value="title") String title,
                                @QueryParam(value="price") String price,
                                @QueryParam(value="sortBy") String sortCriteria) {
        if(!areValidPaginationParameters(start, end))
            return buildErrorResponse(Status.BAD_REQUEST, "validation.incorrect.pagination.range");

        if (!isValidPriceRange(price))
            return buildErrorResponse(Status.BAD_REQUEST, "validation.incorrect.price.range");

        List<Book> books = bookService.getAllBooks(start, end, author, title, price, sortCriteria);

        if (books == null || books.isEmpty())
            return Response.status(Status.NOT_FOUND).build();

        return Response.ok().entity(new GenericEntity<List<Book>>(books) {}).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("{bookId}")
    public Response getBook(@PathParam("bookId") Integer bookId) {
        Book book = bookService.getBook(bookId);

        if(book == null)
            return Response.status(Status.NOT_FOUND).build();

        return Response.ok().entity(book).build();
    }

    @GET
    @Produces("image/jpeg")
    @Path("{bookId}")
    public Response getBookCover(@PathParam("bookId") Integer bookId) {
        File bookCover = bookService.getBookCover(bookId);

        if (bookCover == null)
            return Response.status(Status.NOT_FOUND).build();

        return Response.ok().entity(bookCover).build();
    }

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("size")
    public Response getBooksCount() {
        Long booksCount = bookService.getBooksCount();

        return Response.ok().entity(booksCount).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        if (book.getTitle() == null || book.getTitle().length() == 0)
            return buildErrorResponse(Status.BAD_REQUEST, "validation.missing.title");

        Book newBook = bookService.createBook(book);

        if (newBook == null)
            return buildErrorResponse(Status.BAD_REQUEST, "book.already.exists");

        String uri = uriInfo.getAbsolutePath() + "/" + newBook.getId();

        return Response.ok().entity(newBook).location(URI.create(uri)).build();
    }

    @PUT
    @Path("{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateBook(@PathParam("bookId") Integer bookId, Book book) {
        Book updatedBook = bookService.updateBook(bookId, book);

        if(updatedBook == null)
            return Response.status(Status.NOT_FOUND).build();

        return Response.ok().entity(updatedBook).build();
    }

    @DELETE
    @Path("{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteBook (@PathParam ("bookId") Integer bookId) {
        boolean bookWasDeleted = bookService.deleteBook(bookId);

        if(!bookWasDeleted)
            return Response.status(Status.NOT_FOUND).build();

        return Response.ok().build();
    }

    private boolean isValidPriceRange(String price) {
        if (price == null)
            return true;

        String[] priceBounds = price.split(",");
        if (priceBounds.length != 2)
            return false;

        if (!isPositiveInteger(priceBounds[0]) || !isPositiveInteger(priceBounds[1]))
            return false;

        return true;
    }

    private boolean areValidPaginationParameters(String start, String end) {
        if (start == null && end == null)
            return true;

        if (start != null && !isPositiveInteger(start))
            return false;

        if (end != null && !isPositiveInteger(end))
            return false;

        if (start != null && end != null && Integer.valueOf(start) > Integer.valueOf(end))
                return false;

        return true;
    }

    private boolean isPositiveInteger(String number) {
        try {
            int integer = Integer.parseInt(number);
            if (integer >= 0)
                return true;
        } catch (NumberFormatException e) {
        }

        return false;
    }

    private Response buildErrorResponse(Status status, String errorMessage) {
        ErrorBean error = new ErrorBean();
        error.setErrorCode(errorMessage);

        return Response.status(status).type(MediaType.APPLICATION_JSON).entity(error).build();
    }

    @GET
    @Path("{bookId}/reviews")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllReviews(@PathParam("bookId") Integer bookId) {
        List<Review> reviews = reviewService.getAllReviews(bookId);

        if (reviews == null || reviews.isEmpty())
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(new GenericEntity<List<Review>>(reviews) {}).build();
    }

    @GET
    @Path("{bookId}/reviews/{reviewId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getReview(@PathParam("bookId") Integer bookId, @PathParam("reviewId") Integer reviewId) {
        Review review = reviewService.getReviewById(bookId, reviewId);

        if (review == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(review).build();
    }

    @PUT
    @Path("{bookId}/reviews/{reviewId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateReview(@PathParam("bookId") Integer bookId, @PathParam("reviewId") Integer reviewId, Review review) {
        Review updatedReview = reviewService.updateReview(bookId, reviewId, review);

        if(updatedReview == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().entity(updatedReview).build();
    }

    @POST
    @Path("{bookId}/reviews")
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
    @Path("{bookId}/reviews/{reviewId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteReview(@PathParam("bookId") Integer bookId, @PathParam("reviewId") Integer reviewId) {
        boolean reviewWasDeleted = reviewService.deleteBookReview(bookId, reviewId);

        if(!reviewWasDeleted)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok().build();
    }
}