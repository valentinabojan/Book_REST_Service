package unit_tests.resources;

import business_layer.entity.Book;
import business_layer.value_objects.ErrorBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import application_layer.BookResource;
import business_layer.services.BookService;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;

public class BookResourceTest {

    private BookResource bookResource;
    private BookService mockBookService;
    private final static Integer BOOK_ID = 1;
    private final static String BOOK_COVER_PATH = "book1";

    @Before
    public void setUp() {
        mockBookService = Mockito.mock(BookService.class);
        bookResource = new BookResource();
        bookResource.setBookService(mockBookService);
    }

    @Test
    public void givenAWrongBookId_getBookById_returns404NOTFOUND() {
        Mockito.when(mockBookService.getBook(BOOK_ID)).thenReturn(null);

        Response response = bookResource.getBook(BOOK_ID);

        Mockito.verify(mockBookService, times(1)).getBook(BOOK_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Status.NOT_FOUND);
        assertThat(response.getEntity()).isEqualTo(null);
    }

    @Test
    public void givenAValidBookId_getBookById_returns200OK() {
        Book expectedBook = new Book();
        Mockito.when(mockBookService.getBook(BOOK_ID)).thenReturn(expectedBook);

        Response response = bookResource.getBook(BOOK_ID);

        Mockito.verify(mockBookService, times(1)).getBook(BOOK_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Status.OK);
    }

    @Test
    public void givenAValidBookId_getBookById_returnsCorrectBook() {
        Book expectedBook = new Book();
        Mockito.when(mockBookService.getBook(BOOK_ID)).thenReturn(expectedBook);

        Response response = bookResource.getBook(BOOK_ID);

        Mockito.verify(mockBookService, times(1)).getBook(BOOK_ID);
        assertThat(response.getEntity()).isEqualTo(expectedBook);
    }

    @Test
    public void givenAValidBookId_getBookCover_returns200OK() {
        File expectedBookCover = new File(BOOK_COVER_PATH);
        Mockito.when(mockBookService.getBookCover(BOOK_ID)).thenReturn(expectedBookCover);

        Response response = bookResource.getBookCover(BOOK_ID);

        Mockito.verify(mockBookService, times(1)).getBookCover(BOOK_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Status.OK);
    }

    @Test
    public void givenAValidBookId_getBookCover_returnsCorrectBookCover() {
        File expectedBookCover = new File(BOOK_COVER_PATH);
        Mockito.when(mockBookService.getBookCover(BOOK_ID)).thenReturn(expectedBookCover);

        Response response = bookResource.getBookCover(BOOK_ID);

        Mockito.verify(mockBookService, times(1)).getBookCover(BOOK_ID);
        assertThat(response.getEntity()).isEqualTo(expectedBookCover);
    }

    @Test
    public void givenAWrongBookId_getBookCover_returns404NOTFOUND() {
        Mockito.when(mockBookService.getBookCover(BOOK_ID)).thenReturn(null);

        Response response = bookResource.getBookCover(BOOK_ID);

        Mockito.verify(mockBookService, times(1)).getBookCover(BOOK_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Status.NOT_FOUND);
        assertThat(response.getEntity()).isEqualTo(null);
    }

    @Test
    public void givenAValidBookId_deleteBook_returns200OK() {
        Mockito.when(mockBookService.deleteBook(BOOK_ID)).thenReturn(true);

        Response response = bookResource.deleteBook(BOOK_ID);

        Mockito.verify(mockBookService, times(1)).deleteBook(BOOK_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Status.OK);
    }

    @Test
    public void givenAWrongBookId_deleteBook_returns404NOTFOUND() {
        Mockito.when(mockBookService.deleteBook(BOOK_ID)).thenReturn(false);

        Response response = bookResource.deleteBook(BOOK_ID);

        Mockito.verify(mockBookService, times(1)).deleteBook(BOOK_ID);
        assertThat(response.getStatusInfo()).isEqualTo(Status.NOT_FOUND);
        assertThat(response.getEntity()).isEqualTo(null);
    }

    @Test
    public void getBooksSize_returns200OK() {
        int bookSize = 25;
        Mockito.when(mockBookService.getBooksCount()).thenReturn(bookSize);

        Response response = bookResource.getBooksCount();

        Mockito.verify(mockBookService, times(1)).getBooksCount();
        assertThat(response.getStatusInfo()).isEqualTo(Status.OK);
    }

    @Test
    public void getBooksSize_returnsCorrectSize() {
        int bookSize = 25;
        Mockito.when(mockBookService.getBooksCount()).thenReturn(bookSize);

        Response response = bookResource.getBooksCount();

        Mockito.verify(mockBookService, times(1)).getBooksCount();
        assertThat(response.getEntity()).isEqualTo(bookSize);
    }

    @Test
    public void givenABookWithoutTitle_createBook_returns400BADREQUEST() {
        Book book = new Book();

        Response response = bookResource.createBook(book, null);

        assertThat(response.getStatusInfo()).isEqualTo(Status.BAD_REQUEST);
    }

    @Test
    public void givenABookWithoutTitle_createBook_returnsCorrectErrorMessage() {
        Book book = new Book();

        Response response = bookResource.createBook(book, null);

        assertThat(((ErrorBean)response.getEntity()).getErrorCode()).isEqualTo("validation.missing.title");
    }

    @Test
    public void givenAValidBook_createBook_returns200OK() {
        Book book = new Book();
        book.setTitle("title");
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockBookService.createBook(book)).thenReturn(book);

        Response response = bookResource.createBook(book, mockUriInfo);

        Mockito.verify(mockBookService, times(1)).createBook(book);
        assertThat(response.getStatusInfo()).isEqualTo(Status.OK);
    }

    @Test
    public void givenAValidBook_createBook_returnsCorrectBook() {
        Book book = new Book();
        book.setTitle("title");
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockBookService.createBook(book)).thenReturn(book);

        Response response = bookResource.createBook(book, mockUriInfo);

        Mockito.verify(mockBookService, times(1)).createBook(book);
        assertThat(response.getEntity()).isEqualTo(book);
    }

    @Test
    public void givenAValidBook_createBook_returnsLinkToTheNewBook() {
        Book book = new Book();
        book.setTitle("title");
        book.setId(1);
        UriInfo mockUriInfo = Mockito.mock(UriInfo.class);
        Mockito.when(mockBookService.createBook(book)).thenReturn(book);
        Mockito.when(mockUriInfo.getAbsolutePath()).thenReturn(URI.create("http://localhost:8080/webapi/books"));

        Response response = bookResource.createBook(book, mockUriInfo);

        Mockito.verify(mockBookService, times(1)).createBook(book);
        Mockito.verify(mockUriInfo, times(1)).getAbsolutePath();
        assertThat(response.getLocation()).isEqualTo(URI.create("http://localhost:8080/webapi/books/1"));
    }

    @Test
    public void givenAWrongBookId_updateBook_returns404NOTFOUND() {
        Book book = new Book();
        Mockito.when(mockBookService.updateBook(BOOK_ID, book)).thenReturn(null);

        Response response = bookResource.updateBook(BOOK_ID, book);

        Mockito.verify(mockBookService, times(1)).updateBook(BOOK_ID, book);
        assertThat(response.getStatusInfo()).isEqualTo(Status.NOT_FOUND);
        assertThat(response.getEntity()).isEqualTo(null);
    }

    @Test
    public void givenAValidBookIdAndABook_updateBook_returns200OK() {
        Book book = new Book();
        Mockito.when(mockBookService.updateBook(BOOK_ID, book)).thenReturn(book);

        Response response = bookResource.updateBook(BOOK_ID, book);

        Mockito.verify(mockBookService, times(1)).updateBook(BOOK_ID, book);
        assertThat(response.getStatusInfo()).isEqualTo(Status.OK);
    }

    @Test
    public void givenAValidBookIdAndABook_updateBook_returnsUpdatedBook() {
        Book book = new Book();
        Mockito.when(mockBookService.updateBook(BOOK_ID, book)).thenReturn(book);

        Response response = bookResource.updateBook(BOOK_ID, book);

        Mockito.verify(mockBookService, times(1)).updateBook(BOOK_ID, book);
        assertThat(response.getEntity()).isEqualTo(book);
    }

    @Test
    public void givenNoParametersAndNoBooksInDB_getAllBooks_returns404NOTFOUND() {
        Mockito.when(mockBookService.getAllBooks(null, null, null, null, null, null)).thenReturn(null);

        Response response = bookResource.getAllBooks(null, null, null, null, null, null);

        Mockito.verify(mockBookService, times(1)).getAllBooks(null, null, null, null, null, null);
        assertThat(response.getStatusInfo()).isEqualTo(Status.NOT_FOUND);
    }

    @Test
    public void givenNoParameters_getAllBooks_returns200OK() {
        Book book = new Book();
        List<Book> books = Arrays.asList(book);
        Mockito.when(mockBookService.getAllBooks(null, null, null, null, null, null)).thenReturn(books);

        Response response = bookResource.getAllBooks(null, null, null, null, null, null);

        Mockito.verify(mockBookService, times(1)).getAllBooks(null, null, null, null, null, null);
        assertThat(response.getStatusInfo()).isEqualTo(Status.OK);
    }

    @Test
    public void givenNoParameters_getAllBooks_returnsCorrectBooksList() {
        Book book = new Book();
        List<Book> books = Arrays.asList(book);
        Mockito.when(mockBookService.getAllBooks(null, null, null, null, null, null)).thenReturn(books);

        Response response = bookResource.getAllBooks(null, null, null, null, null, null);

        Mockito.verify(mockBookService, times(1)).getAllBooks(null, null, null, null, null, null);
        assertThat(response.getEntity()).isEqualTo(books);
    }

    @Test
    public void givenWrongPaginationParameters_getAllBooks_returns400BADREQUEST() {
        String start = "10";
        String end = "0";

        Response response = bookResource.getAllBooks(start, end, null, null, null, null);

        assertThat(response.getStatusInfo()).isEqualTo(Status.BAD_REQUEST);
    }

    @Test
    public void givenWrongPaginationParameters_getAllBooks_returnsCorrectErrorMessage() {
        String start = "-10";
        String end = "0";

        Response response = bookResource.getAllBooks(start, end, null, null, null, null);

        assertThat(((ErrorBean)response.getEntity()).getErrorCode()).isEqualTo("validation.incorrect.pagination.range");
    }

    @Test
    public void givenWrongFilterByPriceParameter_getAllBooks_returns400BADREQUEST() {
        String priceRange = "0";

        Response response = bookResource.getAllBooks(null, null, null, null, priceRange, null);

        assertThat(response.getStatusInfo()).isEqualTo(Status.BAD_REQUEST);
    }

    @Test
    public void givenValidParameters_getAllBooks_returns200OK() {
        String start = "0";
        String end = "10";
        String author = "Fowler";
        String title = "refactoring";
        String price = "100,200";
        String sortCriteria = "author,title,year,price";
        Book book = new Book();
        List<Book> books = Arrays.asList(book);
        Mockito.when(mockBookService.getAllBooks(start, end, author, title, price, sortCriteria)).thenReturn(books);

        Response response = bookResource.getAllBooks(start, end, author, title, price, sortCriteria);

        Mockito.verify(mockBookService, times(1)).getAllBooks(start, end, author, title, price, sortCriteria);
        assertThat(response.getStatusInfo()).isEqualTo(Status.OK);
    }

    @Test
    public void givenValidParameters_getAllBooks_returnsCorrectBooksList() {
        String start = "0";
        String end = "10";
        String author = "Fowler";
        String title = "refactoring";
        String price = "100,200";
        String sortCriteria = "author,title,year,price";
        Book book = new Book();
        List<Book> books = Arrays.asList(book);
        Mockito.when(mockBookService.getAllBooks(start, end, author, title, price, sortCriteria)).thenReturn(books);

        Response response = bookResource.getAllBooks(start, end, author, title, price, sortCriteria);

        Mockito.verify(mockBookService, times(1)).getAllBooks(start, end, author, title, price, sortCriteria);
        assertThat(response.getEntity()).isEqualTo(books);
    }


}
