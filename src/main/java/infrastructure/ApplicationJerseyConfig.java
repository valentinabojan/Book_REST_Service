package infrastructure;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("")
public class ApplicationJerseyConfig extends ResourceConfig {
    public ApplicationJerseyConfig() {
        packages("resources");
    }
}