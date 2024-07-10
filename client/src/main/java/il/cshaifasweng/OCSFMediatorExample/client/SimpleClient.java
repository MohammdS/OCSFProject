package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleClient extends AbstractClient {

	private static SimpleClient client = null;
	private List<Movie> movieList = new ArrayList<>();

	private SimpleClient(String host, int port) {
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
				// Assuming toString() format is: Movie{id=1, title='Inception', director='Christopher Nolan', genre='Sci-Fi', releaseYear=2010}
				String[] movieParts = parts[i].split(", ");
				int id = Integer.parseInt(movieParts[0].split("=")[1]);
				String title = movieParts[1].split("'")[1];
				String director = movieParts[2].split("'")[1];
				String genre = movieParts[3].split("'")[1];
				int year = Integer.parseInt(movieParts[4].split("=")[1].replace('}', ' ').trim());
				movieList.add(new Movie(id, title, director, genre, year));
			}
		} else {
			System.out.println("Operation failed: " + response);
		}
	}

	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
		}
		return client;
	}

	public void addMovie(Movie movie) {
		sendMessage(String.format("add|%s|%s|%s|%d", movie.getTitle(), movie.getDirector(), movie.getGenre(), movie.getReleaseYear()));
	}

	public void deleteMovie(int movieId) {
		sendMessage(String.format("delete|%d", movieId));
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

	public static void main(String[] args) {
		SimpleClient client = SimpleClient.getClient();
		try {
			client.openConnection();
			// Add your test logic here
			client.closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
