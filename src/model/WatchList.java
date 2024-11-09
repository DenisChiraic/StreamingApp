package model;

import java.util.ArrayList;
import java.util.List;

public class WatchList {
    private List<Movie> movies;
    private List<Serial> serials;

    public WatchList() {
        this.movies = new ArrayList<>();
        this.serials = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public List<Serial> getSerials() {
        return serials;
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
        System.out.println("Movie added to watchlist: " + movie.getTitle());
    }

    public void addSerial(Serial serial) {
        serials.add(serial);
        System.out.println("Serial added to watchlist: " + serial.getTitle());
    }

    public void removeMovie(Movie movie) {
        if (movies.remove(movie)) {
            System.out.println("Movie removed from watchlist: " + movie.getTitle());
        } else {
            System.out.println("Movie not found in watchlist: ");
        }
    }

    public void removeSerial(Serial serial) {
        if (serials.remove(serial)) {
            System.out.println("Serial removed from watchlist: " + serial.getTitle());
        } else {
            System.out.println("Serial not found in watchlist: ");
        }
    }

    public void displayWatchList() {
        System.out.println("Your watchlist: ");
        System.out.println("Movies:");
        for (Movie movie : movies) {
            System.out.println("- " + movie.getTitle());
        }
        System.out.println("Serials:");
        for (Serial serial : serials) {
            System.out.println("- " + serial.getTitle());
        }
    }
 }

