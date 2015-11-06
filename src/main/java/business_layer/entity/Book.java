package business_layer.entity;

import business_layer.value_objects.LocalDateAdapter;
import data_access_layer.repositories.LocalDateAttributeConverter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@Entity
@NamedQueries({
        @NamedQuery(name="countAll", query = "SELECT COUNT(b) FROM Book b"),
        @NamedQuery(name="findCoverPath", query = "SELECT b.coverPath FROM Book b WHERE b.id = :bookId"),
        @NamedQuery(name="findReviewById", query = "SELECT r FROM Book b JOIN b.reviews r WHERE b.id = :bookId AND r.id = :reviewId")
})
public class Book {

    @Id
    @Column(name = "BOOK_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_app_seq")
    @SequenceGenerator(name = "books_app_seq", sequenceName = "books_app_seq", allocationSize = 1)
    private Integer id;

    private String title;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name="book_author",
//                joinColumns={@JoinColumn(name="BOOK_ID")},
//                inverseJoinColumns={@JoinColumn(name="AUTHOR_ID")})
//    @Cascade(CascadeType.ALL)


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="book_author",
            joinColumns=@JoinColumn(name="book_id"))
    @Column(name="author_name")
    @Fetch(FetchMode.SUBSELECT)
    private List<String> authors;

    @ElementCollection(targetClass = BookCategory.class, fetch = FetchType.EAGER)
    @CollectionTable(name="book_category",
                    joinColumns=@JoinColumn(name="BOOK_ID"))
    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.SUBSELECT)
    private List<BookCategory> categories;

    @Column(name = "RELEASE_DATE")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date;

    private Double price;

    private String isbn;

    private String description;

    @Column(name = "COVER_PATH")
    private String coverPath;

    @Column(name = "PAGE_NUMBER")
    private Integer pagesNumber;

    private String language;

    private Double stars;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "BOOK_ID")
    @Cascade(CascadeType.ALL)
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
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

    public Integer getPagesNumber() {
        return pagesNumber;
    }

    public void setPagesNumber(Integer pagesNumber) {
        this.pagesNumber = pagesNumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!id.equals(book.id)) return false;
        if (!isbn.equals(book.isbn)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + isbn.hashCode();
        return result;
    }

    public static class BookBuilder {
        private Book book;

        private BookBuilder() {
            book = new Book();
            book.setAuthors(new ArrayList<>());
            book.setCategories(new ArrayList<>());
        }

        public BookBuilder withId(Integer id) {
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