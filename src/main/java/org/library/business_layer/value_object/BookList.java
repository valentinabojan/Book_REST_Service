package org.library.business_layer.value_object;

import org.library.business_layer.entity.Book;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class BookList {

    private Integer booksCount;

    private List<Book> books;

    public Integer getBooksCount() {
        return booksCount;
    }

    public void setBooksCount(Integer booksCount) {
        this.booksCount = booksCount;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
