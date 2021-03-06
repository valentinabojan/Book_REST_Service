package org.library.business_layer.service;

import org.library.business_layer.entity.Book;
import org.library.business_layer.value_object.BookList;
import org.library.data_access_layer.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Service("bookService")
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public BookList getAllBooks(String start, String end, String author, String title, String price, String sortCriteria) {
        return bookRepository.findAllBooksWithPaginationAndFilteringAndSorting(start, end, author, title, price, sortCriteria);
    }

    @Transactional(readOnly = true)
    public Book getBook(Integer bookId) {
        return bookRepository.findBookById(bookId);
    }

    public boolean deleteBook(Integer bookId) {
        return bookRepository.deleteBook(bookId);
    }

    @Transactional(readOnly = true)
    public Long getBooksCount() {
        return bookRepository.getBooksCount();
    }

    public Book createBook(Book book) {
        return bookRepository.createBook(book);
    }

    public Book updateBook(Integer bookId, Book book) {
        return bookRepository.updateBook(bookId, book);
    }

    @Transactional(readOnly = true)
    public File getBookCover(Integer bookId) {
        return bookRepository.findBookCover(bookId);
    }

}
