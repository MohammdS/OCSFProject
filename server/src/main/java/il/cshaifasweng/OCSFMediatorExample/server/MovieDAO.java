package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class MovieDAO {
    private SessionFactory sessionFactory;

    public MovieDAO() {
        // Initialize the SessionFactory from the configuration file
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Movie.class);

        // You can add other annotated classes here
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    }

    public Movie addMovie(Movie movie) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(movie);
        transaction.commit();
        session.close();
        return movie;
    }

    public void deleteMovie(int movieId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Movie movie = session.get(Movie.class, movieId);
        if (movie != null) {
            session.delete(movie);
        }
        transaction.commit();
        session.close();
    }

    public Movie getMovieById(int movieId) {
        Session session = sessionFactory.openSession();
        Movie movie = session.get(Movie.class, movieId);
        session.close();
        return movie;
    }

    public List<Movie> getAllMovies() {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        List<Movie> movies = null;

        try {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
            query.from(Movie.class);
            movies = session.createQuery(query).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return movies;
    }
}
