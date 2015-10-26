package builders;

import entities.Book;
import entities.BookCategory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookBuilder {

    private String id;
    private String title;
    private List<String> authors = new ArrayList<>();
    private List<BookCategory> categories = new ArrayList<>();
    private LocalDate date;
    private double price;
    private String isbn;
    private String description;
    private String coverPath;
    private int pagesNumber;
    private String language;
    private double stars;

    public Book build() {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthors(authors);
        book.setCategories(categories);
        book.setDate(date);
        book.setPrice(price);
        book.setIsbn(isbn);
        book.setDescription(description);
        book.setCoverPath(coverPath);
        book.setPagesNumber(pagesNumber);
        book.setLanguage(language);
        book.setStars(stars);

        return book;
    }

    public BookBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public BookBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder withAuthors(String... authors) {
        Collections.addAll(this.authors, authors);
        return this;
    }

    public BookBuilder withCategories(BookCategory... categories) {
        Collections.addAll(this.categories, categories);
        return this;
    }

    public BookBuilder withDate(LocalDate localDate) {
        this.date = localDate;
        return this;
    }

    public BookBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public BookBuilder withISBN(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public BookBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public BookBuilder withCoverPath(String coverPath) {
        this.coverPath = coverPath;
        return this;
    }

    public BookBuilder withPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
        return this;
    }

    public BookBuilder withLanguage(String language) {
        this.language = language;
        return this;
    }

    public BookBuilder withStars(double stars) {
        this.stars = stars;
        return this;
    }
}
