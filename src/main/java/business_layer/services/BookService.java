package business_layer.services;

import business_layer.entity.Book;
import data_access_layer.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service("bookService")
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks(String start, String end, String author, String title, String price, String sortCriteria) {
        return bookRepository.findAllBooksWithPaginationAndFilteringAndSorting(start, end, author, title, price, sortCriteria);
    }

    public Book getBook(Integer bookId) {
        return bookRepository.findBookById(bookId);
    }

    public boolean deleteBook(Integer bookId) {
        return bookRepository.deleteBook(bookId);
    }

    public int getBooksCount() {
        return bookRepository.getBooksCount();
    }

    public Book createBook(Book book) {
        return bookRepository.createBook(book);
    }

    public Book updateBook(Integer bookId, Book book) {
        return bookRepository.updateBook(bookId, book);
    }

    public File getBookCover(Integer bookId) {
        return bookRepository.findBookCover(bookId);
    }

}
