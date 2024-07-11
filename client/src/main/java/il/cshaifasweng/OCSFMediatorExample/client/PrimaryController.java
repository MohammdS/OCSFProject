package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.application.Platform;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PrimaryController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField directorField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker showDatePicker;

    @FXML
    private Spinner<Integer> showTimeHourSpinner;

    @FXML
    private Spinner<Integer> showTimeMinuteSpinner;

    @FXML
    private TextField priceField;

    @FXML
    private CheckBox isOnlineCheckBox;

    @FXML
    private TextField movieIdField;

    @FXML
    private Button addButton;

    @FXML
    private Button showButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button changeShowtimeButton;

    @FXML
    private ListView<String> movieListView;

    private SimpleClient client;

    @FXML
    private void initialize() {
        addButton.setOnAction(event -> handleAddMovie());
        showButton.setOnAction(event -> handleShowMovies());
        deleteButton.setOnAction(event -> handleDeleteMovie());
        changeShowtimeButton.setOnAction(event -> handleChangeShowtime());

        // Initialize spinners for time selection
        showTimeHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 12));
        showTimeMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    }

    public void setClient(SimpleClient client) {
        this.client = client;
    }

    @FXML
    private void handleAddMovie() {
        String title = titleField.getText();
        String director = directorField.getText();
        String description = descriptionField.getText();
        LocalDate showDate = showDatePicker.getValue();
        LocalTime showTime = LocalTime.of(showTimeHourSpinner.getValue(), showTimeMinuteSpinner.getValue());
        LocalDateTime showtime = LocalDateTime.of(showDate, showTime);
        int price = Integer.parseInt(priceField.getText());
        boolean isOnline = isOnlineCheckBox.isSelected();

        Movie movie = new Movie(title, director, description, showtime, price, isOnline);
        client.addMovie(movie);

        clearFields();
    }

    @FXML
    private void handleShowMovies() {
        client.showMovies();
        try {
            Thread.sleep(1000); // Wait for server to respond
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> {
            movieListView.getItems().clear();
            for (Movie movie : client.getMovies()) {
                movieListView.getItems().add(movie.toString());
            }
        });
    }

    @FXML
    private void handleDeleteMovie() {
        // Implement the deletion logic based on title or id as per your requirement
        // client.deleteMovie(movieId);
        handleShowMovies(); // Refresh the list after deletion
    }

    @FXML
    private void handleChangeShowtime() {
        int movieId = Integer.parseInt(movieIdField.getText());
        LocalDate showDate = showDatePicker.getValue();
        LocalTime showTime = LocalTime.of(showTimeHourSpinner.getValue(), showTimeMinuteSpinner.getValue());
        LocalDateTime newShowtime = LocalDateTime.of(showDate, showTime);
        client.changeShowtime(movieId, newShowtime);

        handleShowMovies(); // Refresh the list after changing showtime
    }

    private void clearFields() {
        titleField.clear();
        directorField.clear();
        descriptionField.clear();
        showDatePicker.setValue(null);
        showTimeHourSpinner.getValueFactory().setValue(12);
        showTimeMinuteSpinner.getValueFactory().setValue(0);
        priceField.clear();
        isOnlineCheckBox.setSelected(false);
        movieIdField.clear();
    }
}
