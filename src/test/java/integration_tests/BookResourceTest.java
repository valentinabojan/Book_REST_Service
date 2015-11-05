package integration_tests;

import application_layer.BookResource;
import business_layer.entity.Author;
import business_layer.entity.Book;
import business_layer.entity.BookCategory;
import infrastructure.JerseyConfig;
import infrastructure.SpringConfig;
import infrastructure.WebAppInitializer;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringLifecycleListener;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.filter.RequestContextFilter;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookResourceTest extends JerseyTest {

    private BookServiceClient client;
    private Book book1, book2, book3, book4;

    @Override
    protected Application configure() {
        System.out.println("test");
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

        String author1 = "Diana Gabalon";
        String author2 = "Erich Gamma";
        String author3 = "Richard Helm";
        String author4 = "John Vlissides";

        List<String> authors1 = new ArrayList<>();
        authors1.add(author1);
        List<String> authors2 = new ArrayList<>();
        authors2.add(author2);
        authors2.add(author3);
        authors2.add(author4);

        List<BookCategory> categories1 = new ArrayList<>();
        categories1.add(BookCategory.MYSTERY);
        categories1.add(BookCategory.DRAMA);
        List<BookCategory> categories2 = new ArrayList<>();
        categories2.add(BookCategory.SCIENCE);

        book1 = Book.BookBuilder.book().withTitle("Outlander")
                .withAuthors(authors1)
                .withCategories(categories1)
                .withDate(LocalDate.of(2015, Month.JUNE, 12))
                .withPrice(17.99)
                .withIsbn("1-4028-9462-7")
                .withDescription("A very entertaining book.")
                .withCoverPath("book1.jpeg")
                .withPagesNumber(837)
                .withLanguage("Romanian")
                .withStars(4.5)
                .build();

        book2 = Book.BookBuilder.book().withTitle("Design Patterns")
                .withAuthors(authors2)
                .withCategories(categories2)
                .withDate(LocalDate.of(2012, Month.MARCH, 1))
                .withPrice(59.99)
                .withIsbn("0-201-63361-2")
                .withDescription("Design patterns for everyone.")
                .withCoverPath("book2.jpeg")
                .withPagesNumber(395)
                .withLanguage("English")
                .withStars(5)
                .build();

        book3 = Book.BookBuilder.book().withTitle("Design Patterns")
                .withAuthors(authors2)
                .withPrice(99.99)
                .withDate(LocalDate.of(2012, Month.MARCH, 2))
                .withIsbn("0-201-63361-0")
                .build();

        book4 = Book.BookBuilder.book().withTitle("Design Patterns")
                .withAuthors(new ArrayList<>())
                .withIsbn("0-201-63361-8")
                .withPrice(59.99)
                .withDate(LocalDate.of(2010, Month.MARCH, 3))
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
    public void givenManyBooks_GET_returnsTheCorrectListOfBooks() {
        Book newBook1 = client.post("/books", book1).readEntity(Book.class);
        Book newBook2 = client.post("/books", book2).readEntity(Book.class);
        Book newBook3 = client.post("/books", book3).readEntity(Book.class);
        Book newBook4 = client.post("/books", book4).readEntity(Book.class);

        List<Book> books = client.getAllBooks("0", "10", "Erich Gamma", "Design Patterns", "0,200", "title,author,price,year")
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
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }
}
