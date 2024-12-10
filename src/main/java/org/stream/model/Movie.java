package org.stream.model;

import java.util.UUID;

/**
 * Clasa Movie reprezintă un film, incluzând informații despre titlu, durată și rating.
 */
public class Movie implements Identifiable {
    private UUID id;
    private String title;
    private int duration;
    private double rating;

    /**
     * Constructor pentru clasa Movie.
     *
     * @param title    Titlul filmului.
     * @param duration Durata filmului în minute.
     * @param rating   Rating-ul filmului.
     */
    public Movie(String title, int duration, double rating) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.duration = duration;
        this.rating = rating;
    }
    public Movie(UUID id, String title, int duration, double rating) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.rating = rating;
    }
    /**
     * Returnează rating-ul filmului.
     *
     * @return Rating-ul filmului.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Returnează ID-ul filmului.
     *
     * @return ID-ul filmului.
     */
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Returnează titlul filmului.
     * 
     * @return titlul filmului.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returnează durata filmului.
     *
     * @return Durata filmului în minute.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Adauga movie in HistoryList
     * @param historyList
     */
    public void play(HistoryList historyList) {
        System.out.println("Playing movie: " + title);
        historyList.addContent(title, "Movie");
    }
}
