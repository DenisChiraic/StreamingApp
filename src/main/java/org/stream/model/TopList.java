package org.stream.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clasa TopList gestionează un top 10 de filme și seriale bazat pe rating.
 */
public class TopList {
    private List<Movie> topMovies;
    private List<Serial> topSerials;

    public TopList() {}

    public void updateTopMovies(List<Movie> movies) {
        this.topMovies = movies.stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public void updateTopSerials(List<Serial> serials) {
        this.topSerials = serials.stream()
                .sorted(Comparator.comparingDouble(Serial::getRating).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public void displayTopMovies() {
        System.out.println("Top 10 Movies:");
        if (topMovies != null) {
            topMovies.forEach(movie -> System.out.println("- " + movie.getTitle() + " (Rating: " + movie.getRating() + ")"));
        }
    }

    public void displayTopSerials() {
        System.out.println("Top 10 Serials:");
        if (topSerials != null) {
            topSerials.forEach(serial -> System.out.println("- " + serial.getTitle() + " (Rating: " + serial.getRating() + ")"));
        }
    }

    // Getter methods
    public List<Movie> getMovies() {
        return topMovies;
    }

    public List<Serial> getSerials() {
        return topSerials;
    }
}
