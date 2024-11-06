package model;

public class Episode {
    private String episodeName;
    private int episodeId;
    private int episodeNumber;

    public Episode(String episodeName, int episodeId, int episodeNumber) {
        this.episodeName = episodeName;
        this.episodeId = episodeId;
        this.episodeNumber = episodeNumber;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void nextEpisode() {
        this.episodeNumber++;

    }

}
