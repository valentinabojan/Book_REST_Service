package application_layer.resources;

import business_layer.entities.Book;
import business_layer.services.BookService;
import business_layer.value_objects.ErrorBean;
import data_access_layer.repositories.BookRepositoryStub;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.net.URI;
import java.util.List;

@Path("books")
//@Component
public class BookResource {

    @Autowired
    private BookService bookService;
//    = BookService.getInstance(BookRepositoryStub.getInstance());

    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }


//    public BookResource() {
//    }
//
//    @Autowired
//    public BookResource(BookService bookService) {
//        System.out.println("efefefef" + bookService.getX());
//        this.bookService = bookService;
//    }



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
    public Response getBook(@PathParam("bookId") String bookId) {
        Book book = bookService.getBook(bookId);

        if(book == null)
            return Response.status(Status.NOT_FOUND).build();

        return Response.ok().entity(book).build();
    }

    @GET
    @Produces("image/jpeg")
    @Path("{bookId}")
    public Response getBookCover(@PathParam("bookId") String bookId) {
        File bookCover = bookService.getBookCover(bookId);

        if (bookCover == null)
            return Response.status(Status.NOT_FOUND).build();

        return Response.ok().entity(bookCover).build();
    }

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("size")
    public Response getBooksCount() {
        Integer booksCount = bookService.getBooksCount();

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

        return Response.ok().entity(newBook).link(URI.create(uri), "new_book").build();
    }

    @PUT
    @Path("{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateBook(@PathParam("bookId") String bookId, Book book) {
        Book updatedBook = bookService.updateBook(bookId, book);

        if(updatedBook == null)
            return Response.status(Status.NOT_FOUND).build();

        return Response.ok().entity(updatedBook).build();
    }

    @DELETE
    @Path("{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteBook (@PathParam ("bookId") String bookId) {
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
}