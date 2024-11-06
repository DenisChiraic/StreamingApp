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

