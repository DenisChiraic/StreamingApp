package model;

import controller.DataManager;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Clasa TopList gestioneaza un top 10 de filme si seriale bazate pe raiting
 */

public class TopList {

    /**
     * Afiseaza top 10 filme dupa rating.
     */
    public void displayTopMovies() {
        List<Movie> movies = DataManager.loadMovies("movies.csv");

        List<Movie> topMovies = movies.stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .limit(10)
                .collect(Collectors.toList());

        System.out.println("Top 10 Movies:");
        for (Movie movie : topMovies) {
            System.out.println(movie.getTitle() + " - " + movie.getRating());
        }
    }

    /**
     * Afiseaza top 10 seriale dupa rating.
     */
    public void displayTopSerials() {
        List<Serial> serials = DataManager.loadSerials("serials.csv");

        List<Serial> topSerials = serials.stream()
                .sorted(Comparator.comparingDouble(Serial::getRating).reversed())
                .limit(10)
                .collect(Collectors.toList());

        System.out.println("Top 10 Serials:");
        for (Serial serial : topSerials) {
            System.out.println(serial.getTitle() + " - " + serial.getRating());
        }
    }
}
