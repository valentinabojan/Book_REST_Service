package integration_tests;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringLifecycleListener;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.library.application_layer.resource.BookResource;
import org.library.business_layer.entity.Book;
import org.library.infrastructure.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.filter.RequestContextFilter;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookResourceTest extends JerseyTest {

    private BookServiceClient client;
    private Book book1, book2, book3, book4;
    private static String PATH = "/books";

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

        book1 = UtilsTestSetup.createBook1();
        book2 = UtilsTestSetup.createBook2();
        book3 = UtilsTestSetup.createBook3();
        book4 = UtilsTestSetup.createBook4();
    }

    @After
    public void tearDown() {
        client.deleteAllBooks(PATH);
    }

    @Test
    public void givenABook_GETById_returnsTheCorrectBook() {
        Book book = client.post(PATH, book1).readEntity(Book.class);

        Book foundBook = client.getEntity(PATH + "/" + book.getId()).readEntity(Book.class);

        assertThat(foundBook.getId()).isEqualTo(book.getId());
    }

    @Test
    public void givenManyBooks_GETSize_returnsTheCorrectNumberOfBooks() {
        client.post(PATH, book1).readEntity(Book.class);
        client.post(PATH, book2).readEntity(Book.class);

        int booksNumber = client.getSize(PATH + "/size").readEntity(Integer.class);

        assertThat(booksNumber).isEqualTo(2);
    }

    @Test
    public void givenManyBooks_GET_returnsTheCorrectListOfBooks() {
        Book newBook1 = client.post(PATH, book1).readEntity(Book.class);
        Book newBook2 = client.post(PATH, book2).readEntity(Book.class);
        Book newBook3 = client.post(PATH, book3).readEntity(Book.class);
        Book newBook4 = client.post(PATH, book4).readEntity(Book.class);

        List<Book> books = client.getAllBooks("1", "2", "Erich Gamma", "Design Patterns", "0,200", "title,author,price,year")
                                .readEntity(new GenericType<List<Book>>() {});

        assertThat(books.get(0).getId()).isEqualTo(newBook2.getId());
        assertThat(books.get(1).getId()).isEqualTo(newBook3.getId());
    }

    @Test
    public void givenABook_POST_createsANewBook() {
        Book book = client.post(PATH, book1).readEntity(Book.class);

        assertThat(book.getId()).isNotNull();
    }

    @Test
    public void givenABookIdAndABook_PUT_updatesTheBook() {
        Book book = client.post(PATH, book1).readEntity(Book.class);

        client.put(PATH + "/" + book.getId(), book2);

        Book foundBook = client.getEntity(PATH + "/" + book.getId()).readEntity(Book.class);
        assertThat(foundBook.getTitle()).isEqualTo(book2.getTitle());
    }

    @Test
    public void givenABook_DELETE_deletesTheCorrectBook() {
        Book book = client.post(PATH, book1).readEntity(Book.class);

        client.delete(PATH + "/" + book.getId());

        Response response = client.getEntity(PATH + "/" + book.getId());
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }
}
