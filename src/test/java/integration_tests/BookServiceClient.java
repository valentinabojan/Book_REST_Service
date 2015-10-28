package integration_tests;

import business_layer.entities.Book;
import business_layer.entities.Review;

import javax.net.ssl.SSLEngineResult;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class BookServiceClient {

    private static String TARGET = "http://localhost:8080/webapi/";
    private Client client;

    public BookServiceClient () {
        client = ClientBuilder.newClient();
    }

    public Response getAllBooks(String start, String end, String author, String title, String priceRange, String sortCriteria) {
        return client.target(TARGET).path("books")
                                    .queryParam("start", start).queryParam("end", end).queryParam("author", author).queryParam("title", title)
                                    .request(MediaType.APPLICATION_JSON)
                                    .get(Response.class);
    }

    public Response getAllReviews(String path) {
        return client.target(TARGET).path(path)
                                    .request(MediaType.APPLICATION_JSON)
                                    .get(Response.class);
    }

    public Response getSize(String path) {
        return client.target(TARGET).path(path)
                                    .request(MediaType.TEXT_PLAIN)
                                    .get(Response.class);
    }

    public Response getEntity(String path) {
        return client.target(TARGET).path(path)
                                    .request(MediaType.APPLICATION_JSON)
                                    .get(Response.class);
    }

    public <T> Response post(String path, T entity) {
        return client.target(TARGET).path(path)
                                    .request(MediaType.APPLICATION_JSON)
                                    .post(Entity.entity(entity, MediaType.APPLICATION_JSON));
    }

    public <T> Response put(String path, T entity) {
        return client.target(TARGET).path(path)
                                    .request(MediaType.APPLICATION_JSON)
                                    .put(Entity.entity(entity, MediaType.APPLICATION_JSON));
    }

    public Response delete(String path) {
        return client.target(TARGET).path(path)
                                    .request(MediaType.APPLICATION_JSON)
                                    .delete();
    }

    public void deleteAllBooks(String target) {
        Response response = getAllBooks(null, null, null, null, null, null);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Book> books = response.readEntity(new GenericType<List<Book>>() {});
            books.stream().forEach(book -> delete(target + "/" + book.getId()));
        }
    }

    public void deleteAllReviews(String target) {
        Response response = getAllReviews(target);

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            List<Review> reviews = response.readEntity(new GenericType<List<Review>>() {});
            reviews.stream().forEach(review -> delete(target + "/" + review.getId()));
        }
    }
}
