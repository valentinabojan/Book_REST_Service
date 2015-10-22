package repositories;

import entities.Book;

import java.util.List;

public interface BookRepository {

    List<Book> findAllBooks();

    Book findBookById(String bookId);

    boolean deleteBook(String bookId);

    Book createBook(Book book);

    Book updateBook(Book book);
}
