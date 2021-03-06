package org.library.infrastructure;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.annotation.Priority;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

@Priority(value = 1)
public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(SpringConfig.class);

        container.addListener(new ContextLoaderListener(context));
        container.addListener(new RequestContextListener());

        container.setInitParameter("contextConfigLocation", "");

//        FilterRegistration.Dynamic routeFilter = container.addFilter("routeFilter", new RouteFilter());
//        routeFilter.addMappingForUrlPatterns(null, true, "/*");
//        routeFilter.setAsyncSupported(true);
    }
 }