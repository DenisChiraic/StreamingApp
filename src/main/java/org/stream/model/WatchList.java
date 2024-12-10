package org.stream.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clasa WatchList reprezintă lista de filme și seriale pe care utilizatorul
 * dorește să le vizioneze.
 */
public class WatchList {
    private UUID UserID;
    private List<Movie> movies;
    private List<Serial> serials;

    /**
     * Constructor pentru WatchList.
     */
    public WatchList() {
        this.movies = new ArrayList<>();
        this.serials = new ArrayList<>();
    }
    public WatchList(UUID UserID) {
        this.UserID = UserID;
        this.movies = new ArrayList<>();
        this.serials = new ArrayList<>();
    }

    public UUID getUserId() {
        return UserID;
    }
    public void setUserId(UUID userID) {
        UserID = userID;
    }

    /**
     * Returnează lista de filme din WatchList.
     *
     * @return Lista de filme.
     */
    public List<Movie> getMovies() {
        return movies;
    }

    /**
     * Returnează lista de seriale din WatchList.
     *
     * @return Lista de seriale.
     */
    public List<Serial> getSerials() {
        return serials;
    }

    /**
     * Adaugă un film în lista de vizionare.
     *
     * @param movie Filmul de adăugat.
     */
    public void addMovie(Movie movie) {
        movies.add(movie);
        System.out.println("Movie added to watchlist: " + movie.getTitle());
    }

    /**
     * Adaugă un serial în lista de vizionare.
     *
     * @param serial Serialul de adăugat.
     */
    public void addSerial(Serial serial) {
        serials.add(serial);
        System.out.println("Serial added to watchlist: " + serial.getTitle());
    }

    /**
     * Elimină un film din lista de vizionare.
     *
     * @param movie Filmul de eliminat.
     */
    public void removeMovie(Movie movie) {
        if (movies.remove(movie)) {
            System.out.println("Movie removed from watchlist: " + movie.getTitle());
        } else {
            System.out.println("Movie not found in watchlist: ");
        }
    }

    /**
     * Elimină un serial din lista de vizionare.
     *
     * @param serial Serialul de eliminat.
     */
    public void removeSerial(Serial serial) {
        if (serials.remove(serial)) {
            System.out.println("Serial removed from watchlist: " + serial.getTitle());
        } else {
            System.out.println("Serial not found in watchlist: ");
        }
    }

    /**
     * Afișează conținutul listei de vizionare.
     */
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

