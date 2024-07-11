package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SimpleClient extends AbstractClient {

	private List<Movie> movieList = new ArrayList<>();

	public SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		} else if (msg instanceof String) {
			handleServerResponse((String) msg);
		}
	}

	private void handleServerResponse(String response) {
		String[] parts = response.split("\\|");
		if (parts[0].equals("success")) {
			System.out.println("Operation successful");
			movieList.clear();
			for (int i = 1; i < parts.length; i++) {
				System.out.println(parts[i]);
				String[] movieParts = parts[i].split(", ");
				int id = Integer.parseInt(movieParts[0].split("=")[1]);
				String title = movieParts[1].split("'")[1];
				String director = movieParts[2].split("'")[1];
				String description = movieParts[3].split("'")[1];
				LocalDateTime showtime = LocalDateTime.parse(movieParts[4].split("'")[1]);
				int price = Integer.parseInt(movieParts[5].split("=")[1]);
				boolean isOnline = Boolean.parseBoolean(movieParts[6].split("=")[1].replace('}', ' ').trim());
				movieList.add(new Movie(id, title, director, description, showtime, price, isOnline));
			}
		} else {
			System.out.println("Operation failed: " + response);
		}
	}

	public void addMovie(Movie movie) {
		sendMessage(String.format("add|%s|%s|%s|%s|%d|%b", movie.getTitle(), movie.getDirector(), movie.getDescription(), movie.getShowtime().toString(), movie.getPrice(), movie.isOnline()));
	}

	public void deleteMovie(int movieId) {
		sendMessage(String.format("delete|%d", movieId));
	}

	public void changeShowtime(int movieId, LocalDateTime newShowtime) {
		sendMessage(String.format("changeShowtime|%d|%s", movieId, newShowtime.toString()));
	}

	public void showMovies() {
		sendMessage("show");
	}

	private void sendMessage(String message) {
		try {
			sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Movie> getMovies() {
		return movieList;
	}
}
