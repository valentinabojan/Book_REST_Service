package repositories;

import entities.Book;
import entities.BookCategory;
import entities.Review;

import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookStubRepository implements BookRepository {

    private List<Book> books;
    private List<Review> reviews;

    private static BookRepository bookRepositoryInstance;

    private BookStubRepository() {
        books = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    public static BookRepository getInstance() {
        if (bookRepositoryInstance == null)
            bookRepositoryInstance = new BookStubRepository();
        return bookRepositoryInstance;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public List<Book> findAllBooks() {
        return books;
    }

    @Override
    public Book findBookById(String bookId) {
        return books.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElse(null);
    }

    @Override
    public boolean deleteBook(String bookId) {
        return books.removeIf(book -> book.getId().equals(bookId));
    }

    @Override
    public Book createBook(Book book) {
        book.setId((books.size() + 1) + "");
        books.add(book);

        return book;
    }

    @Override
    public Book updateBook(String bookId, Book book) {
        Book updatedBook = findBookById(bookId);

        if (updatedBook == null)
            return null;

        book.setId(updatedBook.getId());
        books.remove(updatedBook);
        books.add(book);

        return book;
    }

    @Override
    public List<Book> getAllBooksWithPaginationAndFilteringAndSorting(String start, String end,
                                                                      String author, String title, String price,
                                                                      String sortCriteria) {
        Stream<Book> paginatedBooks = paginateBooks(books.stream(), start, end);

        Stream<Book> filteredBooks = filterBooks(paginatedBooks, author, title, price);

        Stream<Book> sortedBooks = sortBooks(filteredBooks, sortCriteria);

        return sortedBooks.collect(Collectors.toList());
    }

    @Override
    public File findBookCover(String bookId) {
        String coverPath = books.stream().filter(book -> book.getId().equals(bookId)).findFirst().map(Book::getCoverPath).orElse(null);

        if (coverPath != null)
            return new File(getClass().getClassLoader().getResource(coverPath).getFile());

        return null;
    }

    private Stream<Book> paginateBooks(Stream<Book> books, String start, String end) {
        long firstBook = start == null ? 0 : Long.valueOf(start);

        if (end == null)
            return books.skip(firstBook);
        return books.skip(firstBook).limit(Long.valueOf(end) - firstBook + 1);
    }

    private Stream<Book> filterBooks(Stream<Book> books, String author, String title, String price) {
        if (author != null)
            books = books.filter(book -> book.getAuthors().stream()
                    .anyMatch(bookAuthor -> bookAuthor.toLowerCase().contains(author.toLowerCase())));

        if (title != null)
            books = books.filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()));

        if (price != null) {
            String[] priceRange = price.split(",");
            int lowPriceBound = Integer.valueOf(priceRange[0]);
            int highPriceBound = Integer.valueOf(priceRange[1]);
            books = books.filter(book -> book.getPrice() >= lowPriceBound && book.getPrice() <= highPriceBound);
        }

        return books;
    }

    private Stream<Book> sortBooks(Stream<Book> books, String sortCriteria) {
        if (sortCriteria == null)
            return books;

        Comparator<Book> bookComparator = Comparator.comparing(book -> 0);


        Stream<Book> sortedBooks = books;
        for (String criteria : sortCriteria.split(",")) {
            System.out.println(criteria);
            bookComparator = bookComparator.thenComparing(getComparatorByCriteria(criteria));
        }

        return sortedBooks.sorted(bookComparator);
    }

    private Comparator<Book> getComparatorByCriteria(String criteria) {
        switch (criteria) {
            case "title":
                return Comparator.comparing(Book::getTitle);
            case "price":
                return Comparator.comparingDouble(Book::getPrice);
            case "year":
                return getComparatorByBookPublishYear();
            case "author":
                return getComparatorByBookAuthors();
            default:
                return Comparator.comparing(book -> 0);
        }
    }

    private Comparator<Book> getComparatorByBookPublishYear() {
        return (book1, book2) -> {
            Integer book1PublishYear = book1.getDate().getYear();
            Integer book2PublishYear = book2.getDate().getYear();
            return book1PublishYear.compareTo(book2PublishYear);
        };
    }

    private Comparator<Book> getComparatorByBookAuthors() {
        return (book1, book2) -> {
            List<String> book1Authors = book1.getAuthors();
            List<String> book2Authors = book2.getAuthors();

            Collections.sort(book1Authors);
            Collections.sort(book2Authors);

            for (int i = 0; i < Math.min(book1Authors.size(), book2Authors.size()); i++) {
                int comparingResult = book1Authors.get(i).compareTo(book2Authors.get(i));
                if (comparingResult != 0)
                    return comparingResult;
            }

            return 0;
        };
    }

    @Override
    public List<Review> findAllBookReviews(String bookId) {
        return reviews.stream().filter(review -> review.getBookId().equals(bookId)).collect(Collectors.toList());
    }

    @Override
    public Review findReviewById(String bookId, String reviewId) {
        return reviews.stream()
                .filter(review -> review.getBookId().equals(bookId) && review.getId().equals(reviewId))
                .findFirst().orElse(null);
    }

    @Override
    public boolean deleteBookReview(String bookId, String reviewId) {
        return reviews.removeIf(review -> review.getBookId().equals(bookId) && review.getId().equals(reviewId));
    }

    @Override
    public Review updateReview(String bookId, String reviewId, Review review) {
        Review updatedReview = findReviewById(bookId, reviewId);

        if (updatedReview == null)
            return null;

        review.setId(updatedReview.getId());
        review.setBookId(bookId);
        reviews.remove(updatedReview);
        reviews.add(review);

        return review;
    }

    @Override
    public Review createReview(String bookId, Review review) {
        review.setBookId(bookId);
        review.setId((reviews.size() + 1) + "");
        reviews.add(review);

        return review;
    }

}