package org.library.business_layer.entity;

import org.library.data_access_layer.data_converter.LocalDateAdapter;
import org.library.data_access_layer.data_converter.LocalDateAttributeConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement
@Entity
public class Review {

    @Id
    @Column(name = "REVIEW_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "books_app_seq")
    @SequenceGenerator(name = "books_app_seq", sequenceName = "books_app_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "USERNAME")
    private String user;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "REVIEW_DATE")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public static class ReviewBuilder {
        private Review review;

        private ReviewBuilder() {
            review = new Review();
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

        public static ReviewBuilder review() {
            return new ReviewBuilder();
        }

        public Review build() {
            return review;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (id != null ? !id.equals(review.id) : review.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
