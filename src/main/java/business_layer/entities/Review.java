package business_layer.entities;

import business_layer.value_objects.LocalDateAdapter;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement
public class Review {
    private String id;
    private String user;
    private String title;
    private String content;
    private LocalDate date;
    private String bookId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public static class ReviewBuilder {
        private Review review;

        private ReviewBuilder() {
            review = new Review();
        }

        public ReviewBuilder withId(String id) {
            review.id = id;
            return this;
        }

        public ReviewBuilder withUser(String user) {
            review.user = user;
            return this;
        }

        public ReviewBuilder withTitle(String title) {
            review.title = title;
            return this;
        }

        public ReviewBuilder withContent(String content) {
            review.content = content;
            return this;
        }

        public ReviewBuilder withDate(LocalDate date) {
            review.date = date;
            return this;
        }

        public ReviewBuilder withBookId(String bookId) {
            review.bookId = bookId;
            return this;
        }

        public static ReviewBuilder review() {
            return new ReviewBuilder();
        }

        public Review build() {
            return review;
        }
    }
}
