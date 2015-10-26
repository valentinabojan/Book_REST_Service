package unit_tests.repositories;

import builders.BookBuilder;
import entities.Book;
import entities.BookCategory;
import org.junit.Before;
import org.junit.Test;
import repositories.BookRepository;
import repositories.BookRepositoryStub;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class BookRepositoryStubTest {

    private BookRepository bookRepository;
    private Book book1, book2, book3, book4;

    @Before
    public void setUp() {
        bookRepository = BookRepositoryStub.getInstance();

        book1 = new BookBuilder().withTitle("Outlander")
                                .withAuthors("Diana Gabalon")
                                .withCategories(BookCategory.MYSTERY, BookCategory.DRAMA)
                                .withDate(LocalDate.of(2015, Month.JUNE, 12))
                                .withPrice(17.99)
                                .withISBN("1-4028-9462-7")
                                .withDescription("A very entertaining book.")
                                .withCoverPath("book1.jpeg")
                                .withPagesNumber(837)
                                .withLanguage("Romanian")
                                .withStars(4.5)
                                .build();

        book2 = new BookBuilder().withTitle("Design Patterns")
                                .withAuthors("Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides")
                                .withCategories(BookCategory.SCIENCE)
                                .withDate(LocalDate.of(2012, Month.MARCH, 1))
                                .withPrice(59.99)
                                .withISBN("0-201-63361-2")
                                .withDescription("Design patterns for everyone.")
                                .withCoverPath("book2.jpeg")
                                .withPagesNumber(395)
                                .withLanguage("English")
                                .withStars(5)
                                .build();

        book3 = new BookBuilder().withTitle("Design Patterns")
                                .withAuthors("Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides")
                                .withPrice(99.99)
                                .build();

        book4 = new BookBuilder().withTitle("Design Patterns")
                                .withPrice(79.99)
                                .build();
    }

    @Test
    public void countBooks_returnsCorrectSize() {
        Book book = bookRepository.createBook(book1);

        int booksCount = bookRepository.getBooksCount();

        assertThat(booksCount).isEqualTo(1);

        bookRepository.deleteBook(book.getId());
    }

    @Test
    public void findBookById_returnsCorrectBook() {
        Book book = bookRepository.createBook(book1);

        Book foundBook = bookRepository.findBookById(book.getId());

        assertThat(foundBook).isEqualTo(book);

        bookRepository.deleteBook(book.getId());
    }

    @Test
    public void givenNoParameters_getAllBooks_returnsCorrectListOfBooks() {
        Book newBook1 = bookRepository.createBook(book1);
        Book newBook2 = bookRepository.createBook(book2);

        List<Book> books = bookRepository.findAllBooksWithPaginationAndFilteringAndSorting(null, null, null, null, null, null);

        assertThat(books).isEqualTo(Arrays.asList(newBook1, newBook2));

        bookRepository.deleteBook(newBook1.getId());
        bookRepository.deleteBook(newBook2.getId());
    }

    @Test
    public void givenPaginationParameters_getAllBooks_returnsCorrectListOfBooks() {
        Book newBook1 = bookRepository.createBook(book1);
        Book newBook2 = bookRepository.createBook(book2);
        Book newBook3 = bookRepository.createBook(book3);

        List<Book> books = bookRepository.findAllBooksWithPaginationAndFilteringAndSorting("0", "1", null, null, null, null);

        assertThat(books).isEqualTo(Arrays.asList(newBook1, newBook2));

        bookRepository.deleteBook(newBook1.getId());
        bookRepository.deleteBook(newBook2.getId());
        bookRepository.deleteBook(newBook3.getId());
    }

    @Test
    public void givenFilteringParameters_getAllBooks_returnsCorrectListOfBooks() {
        Book newBook1 = bookRepository.createBook(book1);
        Book newBook2 = bookRepository.createBook(book2);
        Book newBook3 = bookRepository.createBook(book3);
        Book newBook4 = bookRepository.createBook(book4);

        List<Book> books = bookRepository.findAllBooksWithPaginationAndFilteringAndSorting(null, null, "Gamma", "Design", "0,200", null);

        assertThat(books).isEqualTo(Arrays.asList(newBook2, newBook3));

        bookRepository.deleteBook(newBook1.getId());
        bookRepository.deleteBook(newBook2.getId());
        bookRepository.deleteBook(newBook3.getId());
        bookRepository.deleteBook(newBook4.getId());
    }

    @Test
    public void givenSortingParameters_getAllBooks_returnsCorrectListOfBooks() {
        Book newBook1 = bookRepository.createBook(book1);
        Book newBook2 = bookRepository.createBook(book2);
        Book newBook3 = bookRepository.createBook(book3);
        Book newBook4 = bookRepository.createBook(book4);

        List<Book> books = bookRepository.findAllBooksWithPaginationAndFilteringAndSorting(null, null, null, null, null, "title,author,price,year");

        assertThat(books).isEqualTo(Arrays.asList(newBook4, newBook2, newBook3, newBook1));

        bookRepository.deleteBook(newBook1.getId());
        bookRepository.deleteBook(newBook2.getId());
        bookRepository.deleteBook(newBook3.getId());
        bookRepository.deleteBook(newBook4.getId());
    }

    @Test
    public void createBook_createsNewBook() {
        Book book = bookRepository.createBook(book1);

        int booksCount = bookRepository.getBooksCount();
        Book foundBook = bookRepository.findBookById(book.getId());
        assertThat(booksCount).isEqualTo(1);
        assertThat(foundBook.getTitle()).isEqualTo(book1.getTitle());

        bookRepository.deleteBook(book.getId());
    }

    @Test
    public void updateBook_updatesTheBook() {
        Book book = bookRepository.createBook(book1);

        bookRepository.updateBook(book.getId(), book2);

        Book foundBook = bookRepository.findBookById(book.getId());
        assertThat(foundBook.getTitle()).isEqualTo(book2.getTitle());

        bookRepository.deleteBook(book.getId());
    }

    @Test
    public void deleteBook_removesTheCorrectBook() {
        Book newBook1 = bookRepository.createBook(book1);
        Book newBook2 = bookRepository.createBook(book2);

        bookRepository.deleteBook(newBook1.getId());

        int booksCount = bookRepository.getBooksCount();
        assertThat(booksCount).isEqualTo(1);

        bookRepository.deleteBook(newBook2.getId());
    }
}