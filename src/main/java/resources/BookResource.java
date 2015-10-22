package resources;

import entities.Book;
import entities.ErrorBean;
import repositories.BookStubRepository;
import services.BookService;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Path("books")
public class BookResource {

    private BookService bookService = BookService.getInstance(BookStubRepository.getInstance());

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllBooks(@QueryParam(value="start") String start,
                                @QueryParam(value="end") String end,
                                @QueryParam(value="author") String author,
                                @QueryParam(value="title") String title,
                                @QueryParam(value="price") String price,
                                @QueryParam(value="sortBy") String sortCriteria) {
        System.out.println(start + " -- " + end + " -- " + sortCriteria + " -- " + price);
        if(start != null && end != null && Integer.valueOf(start) > Integer.valueOf(end)) {
            ErrorBean error = new ErrorBean();
            error.setErrorCode("validation.incorrect.pagination.range");
            return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(error).build();
        }

        if (price != null && price.split(",").length != 2) {
            ErrorBean error = new ErrorBean();
            error.setErrorCode("validation.incorrect.price.range");
            return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(error).build();
        }

        List<Book> books = bookService.getAllBooks(start, end, author, title, price, sortCriteria);

        if (books == null || books.size() == 0)
            return Response.status(Status.NOT_FOUND).build();

        return Response.ok().entity(new GenericEntity<List<Book>>(books) {}).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_OCTET_STREAM})
    @Path("{bookId}")
    public Response getBook(@PathParam("bookId") String bookId) {
        Book book = bookService.getBook(bookId);

        if(book == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().entity(book).build();
    }

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    @Path("size")
    public Response getBooksCount() {
        Integer booksCount = bookService.getBooksCount();

        return Response.ok().entity(booksCount).build();
    }

    @POST
    @Path("book")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        if (book.getTitle() == null || book.getTitle().length() == 0) {
            ErrorBean error = new ErrorBean();
            error.setErrorCode("validation.missing.title");

            return Response.status(Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON).entity(error).build();
        }

        Book newBook = bookService.createBook(book);
        String uri = uriInfo.getBaseUri() + "books/" + newBook.getId();

        return Response.ok().entity(newBook).link(uri, "new book id").build();
    }

    @PUT
    @Path("{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateBook(@PathParam("bookId") String bookId, Book book) {
        System.out.println("gggggg");
        Book updatedBook = bookService.updateBook(book);

        if(updatedBook == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().entity(updatedBook).build();
    }

    @DELETE
    @Path("{bookId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response deleteBook (@PathParam ("bookId") String bookId) {
        boolean bookWasDeleted = bookService.deleteBook(bookId);

        if(!bookWasDeleted) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().build();
    }


}