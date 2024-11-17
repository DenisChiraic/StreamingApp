package model;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Clasa TopList gestioneaza un top 10 de filme si seriale bazate pe raiting
 */


public class TopList {
    private List<Movie> topMovies;
    private List<Serial> topSerials;

    public TopList() {}

    public void updateTopMovies(List<Movie> movies) {
        this.topMovies = movies.stream().sorted(Comparator.comparingDouble(Movie::getRating).reversed()).limit(10)
                .collect(Collectors.toList());
    }

    public void updateTopSerials(List<Serial> serials) {
        this.topSerials = serials.stream().sorted(Comparator.comparingDouble(Serial::getRating).reversed()).limit(10)
                .collect(Collectors.toList());
    }

    public void displayTopMovies() {
        System.out.println("Top 10 movies:");
        topMovies.forEach(movie -> System.out.println("- " + movie.getTitle() + "(Rating: " + movie.getRating() + ")"));
    }

    public void displayTopSerials() {
        System.out.println("Top 10 serials:");
        topSerials.forEach(serial -> System.out.println("- " + serial.getTitle() + "(Rating: " + serial.getRating() + ")"));
    }
}
