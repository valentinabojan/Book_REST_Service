package integration_tests;

import org.library.business_layer.entity.Book;
import org.library.business_layer.value_object.BookList;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class BookServiceClient {
    private WebTarget target;

    public BookServiceClient(WebTarget target) {
        this.target = target;
    }

    public Response getAllBooks(String start, String end, String author, String title, String priceRange, String sortCriteria) {
        return target.path("books")
                    .queryParam("start", start)
                    .queryParam("end", end)
                    .queryParam("author", author)
                    .queryParam("title", title)
                    .queryParam("price", priceRange)
                    .queryParam("sortBy", sortCriteria)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Response.class);
    }

    public Response getAllReviews(String path) {
        return target.path(path)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Response.class);
    }

    public Response getSize(String path) {
        return target.path(path)
                    .request(MediaType.TEXT_PLAIN)
                    .get(Response.class);
    }

    public Response getEntity(String path) {
        return target.path(path)
                    .request(MediaType.APPLICATION_JSON)
                    .get(Response.class);
    }

    public <T> Response post(String path, T entity) {
        return target.path(path)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(entity, MediaType.APPLICATION_JSON));
    }

    public <T> Response put(String path, T entity) {
        return target.path(path)
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(entity, MediaType.APPLICATION_JSON));
    }

    public Response delete(String path) {
        return target.path(path)
                    .request(MediaType.APPLICATION_JSON)
                    .delete();
    }

    public void deleteAllBooks(String target) {
        Response response = getAllBooks(null, null, null, null, null, null);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Book> books = response.readEntity(BookList.class).getBooks();
            books.stream().forEach(book -> delete(target + "/" + book.getId()));
        }
    }
}
