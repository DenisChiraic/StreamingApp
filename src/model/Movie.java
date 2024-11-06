package model;

import java.util.UUID;

public class Movie {
    private UUID id;
    private String title;
    private int duration;
    private double rating;

    public Movie(String title, int duration, double rating) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.duration = duration;
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getDuration() {
        return duration;
    }

    public void play(HistoryList historyList) {
        System.out.println("Playing movie: " + title);
        historyList.addContent(title, "Movie");
    }
}
