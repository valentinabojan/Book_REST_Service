package data_access_layer.repositories;

import business_layer.entity.Author;
import business_layer.entity.Book;
import business_layer.entity.Review;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository("bookRepository")
public class BookRepositoryHibernate implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Book findBookById(Integer bookId) {
        return entityManager.find(Book.class, bookId);
    }

    @Override
    public Long getBooksCount() {
        TypedQuery<Long> query = entityManager.createNamedQuery("countAll", Long.class);

        return query.getSingleResult();
    }

    @Override
    public boolean deleteBook(Integer bookId) {
        Book book = entityManager.find(Book.class, bookId);
        entityManager.remove(book);

        return true;
    }

    @Override
    public Book createBook(Book book) {
        entityManager.persist(book);

        return book;
    }

    @Override
    @Transactional
    public Book updateBook(Integer bookId, Book book) {
        if (book.getReviews() == null)
            book.setReviews(new ArrayList<>());

        book.setId(bookId);
        entityManager.merge(book);

        return book;
    }

    @Override
    public List<Book> findAllBooksWithPaginationAndFilteringAndSorting(String start, String end, String author, String title, String price, String sortCriteria) {
        String statement = "SELECT DISTINCT b FROM Book b LEFT JOIN b.authors a WHERE";

        String conditionalClause = " 1 = 1";

        if (title != null)
            conditionalClause += " AND b.title = '" + title + "'";
        if (author != null)
            conditionalClause += " AND a = '" + author + "'";
        if (price != null) {
            String[] priceRange = price.split(",");
            int lowPriceBound = Integer.valueOf(priceRange[0]);
            int highPriceBound = Integer.valueOf(priceRange[1]);
            conditionalClause += " AND b.price >= " + lowPriceBound + " AND b.price <= " + highPriceBound;
        }

        conditionalClause += " ORDER BY b.id";

        TypedQuery<Book> query = entityManager.createQuery(statement + conditionalClause, Book.class);

        Stream<Book> filteredBooks = query.getResultList().stream();
        Stream<Book> sortedBooks = sortBooks(filteredBooks, sortCriteria);
        Stream<Book> paginatedBooks = paginateBooks(sortedBooks, start, end);

        return paginatedBooks.collect(Collectors.toList());



//
//        if (sortCriteria != null) {
//            List<String> s = new ArrayList<>();
//            for (String criteria : sortCriteria.split(",")) {
//                if (criteria.equals("title"))
//                    s.add("b.title");
//                if (criteria.equals("price"))
//                    s.add("b.price");
//                if (criteria.equals("author"))
//                    s.add("b.authors");
//                if (criteria.equals("year"))
//                    s.add("year(b.date)");
//            }
//
//            conditionalClause += " ORDER BY";
//            for (int i = 0; i < s.size(); i++)
//                if (i == 0)
//                    conditionalClause += " " + s.get(i);
//                else
//                    conditionalClause += ", " + s.get(i);
//        }

    }

    @Override
    public File findBookCover(Integer bookId) {
        TypedQuery<String> query = entityManager.createNamedQuery("findCoverPath", String.class);
        query.setParameter("bookId", bookId);

        String coverPath = query.getSingleResult();

        if (coverPath != null)
            return new File(getClass().getClassLoader().getResource(coverPath).getFile());

        return null;
    }

    @Override
    public List<Review> findAllBookReviews(Integer bookId) {
        Book book = entityManager.find(Book.class, bookId);

        return book.getReviews();
    }

    @Override
    public Review findReviewById(Integer bookId, Integer reviewId) {
        TypedQuery<Review> query = entityManager.createNamedQuery("findReviewById", Review.class);
        query.setParameter("bookId", bookId);
        query.setParameter("reviewId", reviewId);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
//    @Transactional
    public boolean deleteBookReview(Integer bookId, Integer reviewId) {
        Book book = entityManager.find(Book.class, bookId);
        Review review = findReviewById(bookId, reviewId);

        if (review != null) {
            book.getReviews().remove(review);
            entityManager.merge(book);
        }

        return true;
    }

    @Override
    public Review updateReview(Integer bookId, Integer reviewId, Review review) {
        review.setId(reviewId);
        entityManager.merge(review);

        return review;
    }

    @Override
//    @Transactional
    public Review createReview(Integer bookId, Review review) {
        Book book = entityManager.find(Book.class, bookId);

        List<Review> reviews = book.getReviews();
        if (reviews == null)
            reviews = new ArrayList<>();
        reviews.add(review);
        book.setReviews(reviews);

        entityManager.merge(book);

        return reviews.get(reviews.size() - 1);
    }

    private Stream<Book> sortBooks(Stream<Book> books, String sortCriteria) {
        if (sortCriteria == null)
            return books;

        Comparator<Book> bookComparator = Comparator.comparing(book -> 0);

        for (String criteria : sortCriteria.split(","))
            bookComparator = bookComparator.thenComparing(getComparatorByCriteria(criteria));

        return books.sorted(bookComparator);
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
            LocalDate book1Date = book1.getDate();
            LocalDate book2Date = book2.getDate();

            if (book1Date == null && book2Date == null)
                return 0;

            if (book1Date == null)
                return -1;

            if (book2Date == null)
                return 1;

            Integer book1PublishYear = book1Date.getYear();
            Integer book2PublishYear = book2Date.getYear();
            return book1PublishYear.compareTo(book2PublishYear);
        };
    }

    private Comparator<Book> getComparatorByBookAuthors() {
        return (book1, book2) -> {
            List<String> book1Authors = book1.getAuthors();
            List<String> book2Authors = book2.getAuthors();

            if (book1Authors.isEmpty() && book2Authors.isEmpty())
                return 0;

            if (book1Authors.isEmpty())
                return -1;

            if (book2Authors.isEmpty())
                return 1;

            String book1FirstAuthor = book1Authors.get(0);
            String book2FirstAuthor = book2Authors.get(0);
            return book1FirstAuthor.compareTo(book2FirstAuthor);
        };
    }

    private Stream<Book> paginateBooks(Stream<Book> books, String start, String end) {
        long firstBook = start == null ? 0 : Long.valueOf(start);

        if (end == null)
            return books.skip(firstBook);
        return books.skip(firstBook).limit(Long.valueOf(end) - firstBook + 1);
    }
}
