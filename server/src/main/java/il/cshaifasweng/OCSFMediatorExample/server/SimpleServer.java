package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Movie;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

import java.io.IOException;
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
		}
	}

	private void handleAddMovie(String msg, ConnectionToClient client) {
		// Expected format: add|title|director|genre|releaseYear
		String[] parts = msg.split("\\|");
		if (parts.length == 5) {
			String title = parts[1];
			String director = parts[2];
			String genre = parts[3];
			int releaseYear = Integer.parseInt(parts[4]);

			Movie movie = new Movie();
			movie.setTitle(title);
			movie.setDirector(director);
			movie.setGenre(genre);
			movie.setReleaseYear(releaseYear);

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

	private void sendResponse(ConnectionToClient client, String response) {
		try {
			client.sendToClient(response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
