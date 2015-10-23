package repositories;

import entities.Book;
import entities.BookCategory;
import entities.Review;

import java.awt.event.ComponentAdapter;
import java.io.File;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BookStubRepository implements BookRepository {

    private List<Book> books;
    private List<Review> reviews;

    private static BookRepository bookRepositoryInstance;

    private BookStubRepository() {
        books = new ArrayList<>();
        Book book1 = new Book();
        book1.setId("1");                                       book1.setTitle("Outlander");
        book1.setAuthors(Arrays.asList("Diana Gabalon"));       book1.setCategories(Arrays.asList(BookCategory.MYSTERY, BookCategory.DRAMA));
        book1.setDate(LocalDate.of(2015, Month.JUNE, 12));      book1.setPrice(17.99);
        book1.setIsbn("1-4028-9462-7");                         book1.setDescription("A very entertaining book.");
        book1.setCoverPath("book" + book1.getId() + ".jpeg");   book1.setPagesNumber(837);
        book1.setLanguage("Romanian");                          book1.setStars(4.5);
        books.add(book1);

        Book book2 = new Book();
        book2.setId("2");                                       book2.setTitle("Design Patterns");
        book2.setAuthors(Arrays.asList("Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"));
        book2.setCategories(Arrays.asList(BookCategory.SCIENCE));
        book2.setDate(LocalDate.of(2012, Month.MARCH, 1));      book2.setPrice(59.99);
        book2.setIsbn("0-201-63361-2");                         book2.setDescription("Design patterns for everyone.");
        book2.setCoverPath("book" + book2.getId() + ".jpeg");   book2.setPagesNumber(395);
        book2.setLanguage("English");                           book2.setStars(5);
        books.add(book2);

        Book book3 = new Book();
        book3.setId("3");                                       book3.setTitle("Design Patterns");
        book3.setAuthors(Arrays.asList("Erich Gamma", "Ralph Johnson", "John Vlissides", "Richard Helm"));
        book3.setCategories(Arrays.asList(BookCategory.SCIENCE));
        book3.setDate(LocalDate.of(2012, Month.MARCH, 1));      book3.setPrice(59.90);
        book3.setIsbn("0-201-63361-2");                         book3.setDescription("Design patterns for everyone.");
        book3.setCoverPath("book" + book3.getId() + ".jpeg");   book3.setPagesNumber(395);
        book3.setLanguage("English");                           book3.setStars(5);
        books.add(book3);

        reviews = new ArrayList<>();
        Review review1 = new Review();
        review1.setId("1");                                                 review1.setTitle("Very interesting");
        review1.setContent("I liked it very much.");                        review1.setUser("Valentina");
        review1.setDate(LocalDate.of(2015, Month.OCTOBER, 23));             review1.setBookId("1");
        reviews.add(review1);

        Review review2 = new Review();
        review2.setId("2");                                                 review2.setTitle("A little dark");
        review2.setContent("I found some dark and controversial parts");    review2.setUser("Michaela");
        review2.setDate(LocalDate.of(2015, Month.OCTOBER, 23));             review2.setBookId("1");
        reviews.add(review2);
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
        if (sortCriteria == null)
            return books;

        Comparator<Book> bookComparator = Comparator.comparing(book -> 0);


        Stream<Book> sortedBooks = books;
        for (String criteria : sortCriteria.split(",")) {
            System.out.println(criteria);
            bookComparator = bookComparator.thenComparing(getComaparatorByCriteria(criteria));
        }

        return sortedBooks.sorted(bookComparator);
    }

    private Comparator<Book> getComaparatorByCriteria(String criteria) {
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