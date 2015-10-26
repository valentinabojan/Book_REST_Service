package builders;

import entities.Review;

import java.time.LocalDate;

public class ReviewBuilder {
    private String id;
    private String user;
    private String title;
    private String content;
    private LocalDate date;
    private String bookId;

    public Review build() {
        Review review = new Review();
        review.setId(id);
        review.setUser(user);
        review.setTitle(title);
        review.setContent(content);
        review.setDate(date);
        review.setBookId(bookId);

        return review;
    }

    public ReviewBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ReviewBuilder withUser(String user) {
        this.user = user;
        return this;
    }

    public ReviewBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ReviewBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public ReviewBuilder withDate(LocalDate localDate) {
        this.date = localDate;
        return this;
    }

    public ReviewBuilder withBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

}
