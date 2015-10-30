package spring;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

public class MyWebAppInitializer {
// implements WebApplicationInitializer{
//
//    @Override
//    public void onStartup(ServletContext container) {
//        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.register(AppConfig.class);
//
//        container.addListener(new ContextLoaderListener(rootContext));
//        container.addListener(new RequestContextListener());
//
//        container.setInitParameter("contextConfigLocation", "");
//    }

 }