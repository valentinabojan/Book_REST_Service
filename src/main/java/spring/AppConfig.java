package spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"application_layer", "business_layer", "data_access_layer"})
public class AppConfig {
}