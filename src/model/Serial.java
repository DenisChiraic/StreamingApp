package model;

import java.util.List;
import java.util.UUID;

public class Serial {
    private String title;
    private UUID serialId;
    private List<Episode> episodes;
    private double rating;

    public Serial(String serial, List<Episode> episodes, double rating) {
        this.title = serial;
        this.serialId = UUID.randomUUID();
        this.episodes = episodes;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public UUID getSerialId() {
        return serialId;
    }

    public void play(HistoryList historyList, Episode episode) {
        System.out.println("Now playing episode: " + episode.getEpisodeName() + " of " + title);
        historyList.addContent(episode.getEpisodeName(), "Serial");
    }

    public void nextEpisode(Episode currentEpisode){
        currentEpisode.nextEpisode();

    }
}
