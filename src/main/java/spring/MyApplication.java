package spring;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        packages("resources");
    }
}