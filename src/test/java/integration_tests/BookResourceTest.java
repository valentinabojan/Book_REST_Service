package integration_tests;

import org.junit.After;
import business_layer.entities.Book;
import business_layer.entities.BookCategory;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookResourceTest {

    private BookServiceClient client;
    private Book book1, book2, book3, book4;

    @Before
    public void setUpClass() {
        client = new BookServiceClient();

        book1 = Book.BookBuilder.book().withTitle("Outlander")
                                        .withAuthors(Arrays.asList("Diana Gabalon"))
                                        .withCategories(Arrays.asList(BookCategory.MYSTERY, BookCategory.DRAMA))
                                        .withDate(LocalDate.of(2015, Month.JUNE, 12))
                                        .withPrice(17.99)
                                        .withIsbn("1-4028-9462-7")
                                        .withDescription("A very entertaining book.")
                                        .withCoverPath("book1.jpeg")
                                        .withPagesNumber(837)
                                        .withLanguage("Romanian")
                                        .withStars(4.5)
                                        .build();

        book2 =  Book.BookBuilder.book().withTitle("Design Patterns")
                                        .withAuthors(Arrays.asList("Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"))
                                        .withCategories(Arrays.asList(BookCategory.SCIENCE))
                                        .withDate(LocalDate.of(2012, Month.MARCH, 1))
                                        .withPrice(59.99)
                                        .withIsbn("0-201-63361-2")
                                        .withDescription("Design patterns for everyone.")
                                        .withCoverPath("book2.jpeg")
                                        .withPagesNumber(395)
                                        .withLanguage("English")
                                        .withStars(5)
                                        .build();

        book3 =  Book.BookBuilder.book().withTitle("Design Patterns")
                                        .withAuthors(Arrays.asList("Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"))
                                        .withPrice(99.99)
                                        .build();

        book4 =  Book.BookBuilder.book().withTitle("Design Patterns")
                                        .withPrice(79.99)
                                        .build();
    }

    @After
    public void tearDown() {
        client.deleteAllBooks("/books");
    }

    @Test
    public void givenABook_GETById_returnsTheCorrectBook() {
        Book book = client.post("/books", book1).readEntity(Book.class);

        Book foundBook = client.getEntity("/books/" + book.getId()).readEntity(Book.class);

        assertThat(foundBook.getId()).isEqualTo(book.getId());
    }

    @Test
    public void givenManyBooks_GETSize_returnsTheCorrectNumberOfBooks() {
        client.post("/books", book1).readEntity(Book.class);
        client.post("/books", book2).readEntity(Book.class);

        int booksNumber = client.getSize("/books/size").readEntity(Integer.class);

        assertThat(booksNumber).isEqualTo(2);
    }

    @Test
    public void givenManyBooks_GET_returnsTheCorrectListOfBooks() throws UnsupportedEncodingException {
        Book newBook1 = client.post("/books", book1).readEntity(Book.class);
        Book newBook2 = client.post("/books", book2).readEntity(Book.class);
        Book newBook3 = client.post("/books", book3).readEntity(Book.class);
        Book newBook4 = client.post("/books", book4).readEntity(Book.class);

        List<Book> books = client.getAllBooks("0", "10", "Gamma", "Design", "0,200", "title,author,price,year")
                                .readEntity(new GenericType<List<Book>>() {});

        assertThat(books.get(0).getId()).isEqualTo(newBook2.getId());
        assertThat(books.get(1).getId()).isEqualTo(newBook3.getId());
    }

    @Test
    public void givenABook_POST_createsANewBook() {
        Book book = client.post("/books", book1).readEntity(Book.class);

        assertThat(book.getId()).isNotNull();
    }

    @Test
    public void givenABookIdAndABook_PUT_updatesTheBook() {
        Book book = client.post("/books", book1).readEntity(Book.class);

        client.put("/books/" + book.getId(), book2);

        Book foundBook = client.getEntity("/books/" + book.getId()).readEntity(Book.class);
        assertThat(foundBook.getTitle()).isEqualTo(book2.getTitle());
    }

    @Test
    public void givenABook_DELETE_deletesTheCorrectBook() {
        Book book = client.post("/books", book1).readEntity(Book.class);

        client.delete("/books/" + book.getId());

        Response response = client.getEntity("/books/" + book.getId());
        assertThat(response.getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode());
    }
}
