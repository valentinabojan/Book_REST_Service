package services;

import entities.Book;
import repositories.BookRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookService {

    private static BookService bookServiceInstance;

    private BookRepository bookRepository;

    private BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public static BookService getInstance(BookRepository bookRepository) {
        if (bookServiceInstance == null)
            bookServiceInstance = new BookService(bookRepository);
        return bookServiceInstance;
    }

    public List<Book> getAllBooks(String start, String end, String author, String title, String price, String sortCriteria) {
        List<Book> allBooks = bookRepository.findAllBooks();

        Stream<Book> paginatedBooks = paginateBooks(allBooks.stream(), start, end);

        Stream<Book> filteredBooks = filterBooks(paginatedBooks, author, title, price);

        Stream<Book> sortedBooks = sortBooks(filteredBooks, sortCriteria);

        return sortedBooks.collect(Collectors.toList());
    }

    public Book getBook(String bookId) {
        return bookRepository.findBookById(bookId);
    }

    public boolean deleteBook(String bookId) {
        return bookRepository.deleteBook(bookId);
    }

    public int getBooksCount() {
        return bookRepository.findAllBooks().size();
    }

    public Book createBook(Book book) {
        return bookRepository.createBook(book);
    }

    public Book updateBook(String bookId, Book book) {
        return bookRepository.updateBook(bookId, book);
    }

    private Stream<Book> paginateBooks(Stream<Book> books, String start, String end) {
        if (start != null && end != null)
            return books.skip(Integer.valueOf(start)).limit(Integer.valueOf(end) - Integer.valueOf(start) + 1);
        return books;
    }

    private Stream<Book> filterBooks(Stream<Book> books, String author, String title, String price) {
        if (author != null) {
            System.out.println(author);
            books = books.filter(book -> book.getAuthors().stream()
                                                        .anyMatch(bookAuthor -> bookAuthor.toLowerCase().contains(author.toLowerCase())));
        }
        if (title != null) {
            books = books.filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()));
        }
        if (price != null) {
            String[] priceRange = price.split(",");
            int lowPriceBound = Integer.valueOf(priceRange[0]);
            int highPriceBound = Integer.valueOf(priceRange[1]);
            books = books.filter(book -> book.getPrice() >= lowPriceBound && book.getPrice() <= highPriceBound);
        }

        return books;
    }

    private Stream<Book> sortBooks(Stream<Book> books, String sortCriteria) {
        Comparator<Book> bookComparator = Comparator.comparing(book -> 0);


        Stream<Book> sortedBooks = books;
//        for (String criteria : sortCriteria) {
//            System.out.println(criteria);
//            bookComparator = bookComparator.thenComparing(getComaparatorByCriteria(criteria));
//        }

        return sortedBooks.sorted(bookComparator);
    }

    private Comparator<Book> getComaparatorByCriteria(String criteria) {
        if (criteria.contains("title")) {
            System.out.println("ccccccccccccc");
            return Comparator.comparing(Book::getTitle);
        }
        else
            return Comparator.comparingDouble(Book::getPrice);
    }
}
