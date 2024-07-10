package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class SimpleServer extends AbstractServer {

	private MovieDAO movieDAO = new MovieDAO();

	public SimpleServer(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();

		if (msgString.startsWith("#warning")) {
			Warning warning = new Warning("Warning from server!");
			try {
				client.sendToClient(warning);
				System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msgString.startsWith("add")) {
			handleAddMovie(msgString, client);
		} else if (msgString.startsWith("delete")) {
			handleDeleteMovie(msgString, client);
		} else if (msgString.startsWith("show")) {
			handleShowMovies(client);
		} else if (msgString.startsWith("changeShowtime")) {
			handleChangeShowtime(msgString, client);
		}
	}

	private void handleAddMovie(String msg, ConnectionToClient client) {
		// Expected format: add|title|director|description|showtime|price|isOnline
		String[] parts = msg.split("\\|");
		if (parts.length == 7) {
			String title = parts[1];
			String director = parts[2];
			String description = parts[3];
			LocalDateTime showtime = LocalDateTime.parse(parts[4]);
			int price = Integer.parseInt(parts[5]);
			boolean isOnline = Boolean.parseBoolean(parts[6]);

			Movie movie = new Movie(title, director, description, showtime, price, isOnline);

			Movie savedMovie = movieDAO.addMovie(movie);
			sendResponse(client, "success|" + savedMovie.toString());
		} else {
			sendResponse(client, "failure|Invalid add movie request format");
		}
	}

	private void handleDeleteMovie(String msg, ConnectionToClient client) {
		// Expected format: delete|movieId
		String[] parts = msg.split("\\|");
		if (parts.length == 2) {
			int movieId = Integer.parseInt(parts[1]);

			movieDAO.deleteMovie(movieId);
			sendResponse(client, "success");
		} else {
			sendResponse(client, "failure|Invalid delete movie request format");
		}
	}

	private void handleShowMovies(ConnectionToClient client) {
		List<Movie> movies = movieDAO.getAllMovies();
		StringBuilder response = new StringBuilder("success");
		for (Movie movie : movies) {
			response.append("|").append(movie.toString());
		}
		sendResponse(client, response.toString());
	}

	private void handleChangeShowtime(String msg, ConnectionToClient client) {
		// Expected format: changeShowtime|movieId|newShowtime
		String[] parts = msg.split("\\|");
		if (parts.length == 3) {
			int movieId = Integer.parseInt(parts[1]);
			LocalDateTime newShowtime = LocalDateTime.parse(parts[2]);

			Movie movie = movieDAO.getMovieById(movieId);
			if (movie != null) {
				movie.setShowtime(newShowtime);
				movieDAO.updateMovie(movie);
				sendResponse(client, "success|" + movie.toString());
			} else {
				sendResponse(client, "failure|Movie not found");
			}
		} else {
			sendResponse(client, "failure|Invalid change showtime request format");
		}
	}

	private void sendResponse(ConnectionToClient client, String response) {
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
