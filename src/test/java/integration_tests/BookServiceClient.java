package integration_tests;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class BookServiceClient {

    private static String TARGET = "http://localhost:8080/webapi/";
    private Client client;

    public BookServiceClient () {
        client = ClientBuilder.newClient();
    }

    public Response getAllBooks(int start, int end, String author, String title, String priceRange, String sortCriteria) {
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
}
