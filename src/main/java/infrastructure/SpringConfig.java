package infrastructure;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"application_layer", "business_layer", "data_access_layer"})
public class SpringConfig {

    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() {
        System.out.println("ceva");
        return new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
    }

}
