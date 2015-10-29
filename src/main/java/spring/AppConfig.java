package spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"application_layer", "business_layer", "data_access_layer"})
public class AppConfig {

//    @Bean(name = "bookService")
//    public BookService getCustomerService() {
//        BookService customerService = new BookService(getCustomerRepository());
//        //customerService.setCustomerRepository(getCustomerRepository());
//
//        return customerService;
//    }
//
//    @Bean(name = "bookRepository")
//	public BookRepository getCustomerRepository() {
//		return new BookRepositoryStub();
//	}
}