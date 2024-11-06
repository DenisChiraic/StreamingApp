package model;

public class Movie {
    private int id;
    private String title;
    private int duration;

    public Movie(int id, String title, int duration) {
        this.id = id;
        this.title = title;
        this.duration = duration;
    }

    public int getId() {
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
