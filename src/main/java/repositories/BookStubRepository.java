package repositories;

import entities.Book;
import entities.BookCategory;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BookStubRepository implements BookRepository {

    private List<Book> books;

    private static BookRepository bookRepositoryInstance;

    private BookStubRepository() {
        books = new ArrayList<>();
        Book book1 = new Book();
        book1.setId("1");                                       book1.setTitle("Outlander");
        book1.setAuthors(Arrays.asList("Diana Gabalon"));       book1.setCategories(Arrays.asList(BookCategory.MYSTERY, BookCategory.DRAMA));
        book1.setDate(LocalDate.of(2015, Month.JUNE, 12));      book1.setPrice(17.99);
        book1.setIsbn("1-4028-9462-7");                         book1.setDescription("A very entertaining book.");
        book1.setCoverPath(book1.getTitle() + ".jpeg");         book1.setPagesNumber(837);
        book1.setLanguage("Romanian");                          book1.setStars(4.5);
        books.add(book1);


        Book book2 = new Book();
        book2.setId("2");                                       book2.setTitle("Design Patterns");
        book2.setAuthors(Arrays.asList("Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"));
        book2.setCategories(Arrays.asList(BookCategory.SCIENCE));
        book2.setDate(LocalDate.of(2012, Month.MARCH, 1));      book2.setPrice(59.99);
        book2.setIsbn("0-201-63361-2");                         book2.setDescription("Design patterns for everyone.");
        book2.setCoverPath(book2.getTitle() + ".jpeg");         book2.setPagesNumber(395);
        book2.setLanguage("English");                           book2.setStars(5);
        books.add(book2);
    }

    public static BookRepository getInstance() {
        if (bookRepositoryInstance == null)
            bookRepositoryInstance = new BookStubRepository();
        return bookRepositoryInstance;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Book> findAllBooks() {
        return books;
    }

    @Override
    public Book findBookById(String bookId) {
        return books.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElse(null);
    }

    @Override
    public boolean deleteBook(String bookId) {
        return books.removeIf(book -> book.getId().equals(bookId));

    }

    @Override
    public Book createBook(Book book) {
        book.setId((books.size() + 1) + "");
        books.add(book);

        return book;
    }

    @Override
    public Book updateBook(String bookId, Book book) {
        boolean bookFound = books.removeIf(bookToBeUpdated -> bookToBeUpdated.getId().equals(bookId));

        if (!bookFound)
            return null;

        books.add(book);

        return book;
    }


}