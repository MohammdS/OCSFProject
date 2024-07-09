package il.cshaifasweng.OCSFMediatorExample.server;

/*import java.io.IOException;

*
 * Hello world!
 *

public class App 
{
	
	private static SimpleServer server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);
        System.out.println("server is listening...");
        server.listen();
    }
}*/

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.server.MovieDAO;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.List;

public class App {

    private static SessionFactory sessionFactory;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here
        configuration.addAnnotatedClass(Movie.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void printAllMovies(MovieDAO movieDAO) throws Exception {
        List<Movie> movies = movieDAO.getAllMovies();
        for (Movie movie : movies) {
            System.out.print("Id: ");
            System.out.print(movie.getId());
            System.out.print(", Title: ");
            System.out.print(movie.getTitle());
            System.out.print(", Director: ");
            System.out.print(movie.getDirector());
            System.out.print(", Genre: ");
            System.out.print(movie.getGenre());
            System.out.print(", Release Year: ");
            System.out.print(movie.getReleaseYear());
            System.out.print('\n');
        }
    }

    public static void main(String[] args) {
        try {
            sessionFactory = getSessionFactory();
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            // Use MovieDAO to perform database operations
            MovieDAO movieDAO = new MovieDAO();

            // Adding a movie example
            Movie movie1 = new Movie("Inception", "Christopher Nolan", "Sci-Fi", 2010);
            Movie movie2 = new Movie("The Dark Knight", "Christopher Nolan", "Action", 2008);
            Movie movie3 = new Movie("Interstellar", "Christopher Nolan", "Sci-Fi", 2014);
            Movie movie4 = new Movie("The Matrix", "The Wachowskis", "Sci-Fi", 1999);
            Movie movie5 = new Movie("Pulp Fiction", "Quentin Tarantino", "Crime", 1994);
            Movie movie6 = new Movie("The Shawshank Redemption", "Frank Darabont", "Drama", 1994);
            Movie movie7 = new Movie("The Godfather", "Francis Ford Coppola", "Crime", 1972);
            Movie movie8 = new Movie("The Lord of the Rings: The Fellowship of the Ring", "Peter Jackson", "Fantasy", 2001);
            Movie movie9 = new Movie("The Lord of the Rings: The Two Towers", "Peter Jackson", "Fantasy", 2002);
            Movie movie10 = new Movie("The Lord of the Rings: The Return of the King", "Peter Jackson", "Fantasy", 2003);

            movieDAO.addMovie(movie1);
            movieDAO.addMovie(movie2);
            movieDAO.addMovie(movie3);
            movieDAO.addMovie(movie4);
            movieDAO.addMovie(movie5);
            movieDAO.addMovie(movie6);
            movieDAO.addMovie(movie7);
            movieDAO.addMovie(movie8);
            movieDAO.addMovie(movie9);
            movieDAO.addMovie(movie10);

            // Print all movies
            printAllMovies(movieDAO);

            transaction.commit(); // Save everything
        } catch (Exception exception) {
            if (sessionFactory != null) {
                Session session = sessionFactory.getCurrentSession();
                if (session != null && session.getTransaction() != null) {
                    session.getTransaction().rollback();
                }
            }
            System.err.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        }
    }
}

