package org.library.infrastructure;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        packages("org/library/application_layer");
    }
}