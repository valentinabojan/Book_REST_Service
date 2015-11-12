package org.library.data_access_layer.repository;

import org.library.business_layer.entity.Book;
import org.library.business_layer.entity.Review;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
        TypedQuery<Long> query = entityManager.createNamedQuery(Book.COUNT_ALL_QUERY, Long.class);

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
    public Book updateBook(Integer bookId, Book book) {
        if (book.getReviews() == null)
            book.setReviews(new ArrayList<>());

        book.setId(bookId);
        entityManager.merge(book);

        return book;
    }

    @Override
    public List<Book> findAllBooksWithPaginationAndFilteringAndSorting(String start, String end, String author, String title, String price, String sortCriteria) {
        String statement = "SELECT b FROM Book b";

        String filterClause = generateFilterQueryClause(author, title, price);
        String sortClause = generateSortQueryClause(sortCriteria);

        TypedQuery<Book> query = entityManager.createQuery(statement + " " + filterClause + " " + sortClause, Book.class);

        int firstBook = start == null ? 0 : Integer.valueOf(start);
        query.setFirstResult(firstBook);
        if (end != null)
            query.setMaxResults(Integer.parseInt(end) - firstBook + 1);

        return query.getResultList();
    }

    @Override
    public File findBookCover(Integer bookId) {
        TypedQuery<String> query = entityManager.createNamedQuery(Book.FIND_BOOK_COVER_QUERY, String.class);
        query.setParameter("bookId", bookId);

        try {
            String coverPath = "images/" + query.getSingleResult();

            return new File(getClass().getClassLoader().getResource(coverPath).getFile());
        } catch (NoResultException | NullPointerException e) {
            return null;
        }
    }

    @Override
    public List<Review> findAllBookReviews(Integer bookId) {
        Book book = entityManager.find(Book.class, bookId);

        return book.getReviews();
    }

    @Override
    public Review findReviewById(Integer bookId, Integer reviewId) {
        TypedQuery<Review> query = entityManager.createNamedQuery(Book.FIND_REVIEW_BY_ID_QUERY, Review.class);
        query.setParameter("bookId", bookId);
        query.setParameter("reviewId", reviewId);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
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

    private String generateSortQueryClause(String sortCriteria) {
        String sortClause = "ORDER BY ";

        if (sortCriteria == null)
            return sortClause + " b.id";

        List<String> s = new ArrayList<>();
        for (String criteria : sortCriteria.split(",")) {
            if (criteria.equals("title"))
                s.add("b.title");
            if (criteria.equals("price"))
                s.add("b.price");
//                if (criteria.equals("author"))    // TODO Sort books by first author in the list of authors
//                    s.add(" b.authors(0)");
            if (criteria.equals("year"))
                s.add("year(b.date)");
        }

        for (int i = 0; i < s.size(); i++) {
            if (i == 0)
                sortClause += s.get(i);
            else
                sortClause += ", " + s.get(i);
        }

        sortClause += ", b.id";

        return sortClause;
    }

    private String generateFilterQueryClause(String author, String title, String price) {
        String conditionalClause = "WHERE 1 = 1";

        if (title != null)
            conditionalClause += " AND b.title = '" + title + "'";
        if (author != null)
            conditionalClause += " AND '" + author + "' MEMBER OF b.authors";
        if (price != null) {
            String[] priceRange = price.split(",");
            int lowPriceBound = Integer.valueOf(priceRange[0]);
            int highPriceBound = Integer.valueOf(priceRange[1]);
            conditionalClause += " AND b.price >= " + lowPriceBound + " AND b.price <= " + highPriceBound;
        }

        return conditionalClause;
    }
}
