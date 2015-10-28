package business_layer.entities;

import business_layer.value_objects.LocalDateAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Book {

    private String id;
    private String title;
    private List<String> authors;
    private List<BookCategory> categories;
    private LocalDate date;
    private double price;
    private String isbn;
    private String description;
    private String coverPath;
    private int pagesNumber;
    private String language;
    private double stars;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<BookCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<BookCategory> categories) {
        this.categories = categories;
    }

    public LocalDate getDate() {
        return date;
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public int getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(int pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public static class BookBuilder {
        private Book book;

        private BookBuilder() {
            book = new Book();
            book.setAuthors(new ArrayList<>());
            book.setCategories(new ArrayList<>());
        }

        public BookBuilder withId(String id) {
            book.id = id;
            return this;
        }

        public BookBuilder withTitle(String title) {
            book.title = title;
            return this;
        }

        public BookBuilder withAuthors(List<String> authors) {
            book.authors = authors;
            return this;
        }

        public BookBuilder withCategories(List<BookCategory> categories) {
            book.categories = categories;
            return this;
        }

        public BookBuilder withDate(LocalDate date) {
            book.date = date;
            return this;
        }

        public BookBuilder withPrice(double price) {
            book.price = price;
            return this;
        }

        public BookBuilder withIsbn(String isbn) {
            book.isbn = isbn;
            return this;
        }

        public BookBuilder withDescription(String description) {
            book.description = description;
            return this;
        }

        public BookBuilder withCoverPath(String coverPath) {
            book.coverPath = coverPath;
            return this;
        }

        public BookBuilder withPagesNumber(int pagesNumber) {
            book.pagesNumber = pagesNumber;
            return this;
        }

        public BookBuilder withLanguage(String language) {
            book.language = language;
            return this;
        }

        public BookBuilder withStars(double stars) {
            book.stars = stars;
            return this;
        }

        public static BookBuilder book() {
            return new BookBuilder();
        }

        public Book build() {
            return book;
        }
    }
}