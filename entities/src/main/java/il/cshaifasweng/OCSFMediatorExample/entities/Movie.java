package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "director")
    private String director;

    @Column(name = "description")
    private String description;

    @Column(name = "showtime")
    private LocalDateTime showtime;

    @Column(name = "price")
    private int price;

    @Column(name = "is_online")
    private boolean isOnline;

    // Default constructor
    public Movie() {
    }

    // Constructor for creating new movies (ID will be generated)
    public Movie(String title, String director, String description, LocalDateTime showtime, int price, boolean isOnline) {
        this.title = title;
        this.director = director;
        this.description = description;
        this.showtime = showtime;
        this.price = price;
        this.isOnline = isOnline;
    }

    // Constructor for creating movie instances with an existing ID
    public Movie(int id, String title, String director, String description, LocalDateTime showtime, int price, boolean isOnline) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.description = description;
        this.showtime = showtime;
        this.price = price;
        this.isOnline = isOnline;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getShowtime() {
        return showtime;
    }

    public void setShowtime(LocalDateTime showtime) {
        this.showtime = showtime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public String toString() {
        return String.format("Movie{id=%d, title='%s', director='%s', description='%s', showtime='%s', price=%d, isOnline=%b}", id, title, director, description, showtime, price, isOnline);
    }
}
