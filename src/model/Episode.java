package model;

import java.util.UUID;

public class Episode {
    private String episodeName;
    private UUID episodeId;
    private int episodeNumber;

    public Episode(String episodeName, int episodeNumber) {
        this.episodeName = episodeName;
        this.episodeId = UUID.randomUUID();
        this.episodeNumber = episodeNumber;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public UUID getEpisodeId() {
        return episodeId;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void nextEpisode() {
        this.episodeNumber++;

    }

}
