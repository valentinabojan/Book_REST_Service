package data_access_layer.repositories;

import business_layer.entity.Book;
import business_layer.entity.Review;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Repository("bookRepository")
public class BookRepositoryHibernate implements BookRepository {

    @Autowired
    SessionFactory sessionFactory;

    public BookRepositoryHibernate() {
        sessionFactory = new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
    }

    @Override
    public Book findBookById(Integer bookId) {
        Session session = sessionFactory.openSession();

        Book book = (Book)session.get(Book.class, bookId);

        return book;
    }

    @Override
    public boolean deleteBook(Integer bookId) {
        return false;
    }

    @Override
    public Book createBook(Book book) {
        Session session = sessionFactory.openSession();

        session.persist(book);

        System.out.println(book);

        return book;
    }

    @Override
    public Book updateBook(Integer bookId, Book book) {
        return null;
    }

    @Override
    public List<Book> findAllBooksWithPaginationAndFilteringAndSorting(String start, String end, String author, String title, String price, String sortCriteria) {
        return null;
    }

    @Override
    public File findBookCover(Integer bookId) {
        return null;
    }

    @Override
    public List<Review> findAllBookReviews(Integer bookId) {
        return null;
    }

    @Override
    public Review findReviewById(Integer bookId, Integer reviewId) {
        return null;
    }

    @Override
    public boolean deleteBookReview(Integer bookId, Integer reviewId) {
        return false;
    }

    @Override
    public Review updateReview(Integer bookId, Integer reviewId, Review review) {
        return null;
    }

    @Override
    public Review createReview(Integer bookId, Review review) {
        return null;
    }

    @Override
    public int getBooksCount() {
        return 0;
    }
}
