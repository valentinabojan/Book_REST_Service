package integration_tests;

import org.library.business_layer.entity.Book;
import org.library.business_layer.entity.BookCategory;
import org.library.business_layer.entity.Review;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class UtilsTestSetup {

    public static List<String> createOneAuthor() {
        List<String> authors = new ArrayList<>();
        authors.add("Diana Gabalon");
        return authors;
    }

    public static List<String> createThreeAuthors() {
        List<String> authors = new ArrayList<>();
        authors.add("Erich Gamma");
        authors.add("Richard Helm");
        authors.add("John Vlissides");
        return authors;
    }

    public static List<BookCategory> createTwoCategories() {
        List<BookCategory> categories = new ArrayList<>();
        categories.add(BookCategory.MYSTERY);
        return categories;
    }

    public static List<BookCategory> createOneCategory() {
        List<BookCategory> categories = new ArrayList<>();
        categories.add(BookCategory.SCIENCE);
        return categories;
    }

    public static Book createBook1() {
        return Book.BookBuilder.book().withTitle("Outlander")
                .withAuthors(createOneAuthor())
                .withCategories(createTwoCategories())
                .withDate(LocalDate.of(2015, Month.JUNE, 12))
                .withPrice(17.99)
                .withIsbn("1-4028-9462-7")
                .withDescription("A very entertaining book.")
                .withCoverPath("images/book1.jpeg")
                .withPagesNumber(837)
                .withLanguage("Romanian")
                .withStars(4.5)
                .build();
    }

    public static Book createBook2() {
        return Book.BookBuilder.book().withTitle("Design Patterns")
                .withAuthors(createThreeAuthors())
                .withCategories(createOneCategory())
                .withDate(LocalDate.of(2012, Month.MARCH, 1))
                .withPrice(59.99)
                .withIsbn("0-201-63361-2")
                .withDescription("Design patterns for everyone.")
                .withCoverPath("images/book2.jpeg")
                .withPagesNumber(395)
                .withLanguage("English")
                .withStars(5)
                .build();
    }

    public static Book createBook3() {
        return Book.BookBuilder.book().withTitle("Design Patterns")
                .withAuthors(createThreeAuthors())
                .withPrice(99.99)
                .withDate(LocalDate.of(2012, Month.MARCH, 2))
                .withIsbn("0-201-63361-0")
                .build();
    }

    public static Book createBook4() {
        return Book.BookBuilder.book().withTitle("Design Patterns")
                .withAuthors(createThreeAuthors())
                .withIsbn("0-201-63361-8")
                .withPrice(59.99)
                .withDate(LocalDate.of(2010, Month.MARCH, 3))
                .build();
    }

    public static Review createReview1() {
        return Review.ReviewBuilder.review().withTitle("I liked it very much.")
                .withContent("I liked it very much.")
                .withUser("Valentina")
                .withDate(LocalDate.of(2015, Month.OCTOBER, 23))
                .build();
    }

    public static Review createReview2() {
        return Review.ReviewBuilder.review().withTitle("A little dark")
                .withContent("I found some dark and controversial parts")
                .withUser("Michaela")
                .withDate(LocalDate.of(2015, Month.SEPTEMBER, 5))
                .build();
    }
}
