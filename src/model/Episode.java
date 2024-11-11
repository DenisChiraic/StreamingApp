package model;

import java.util.UUID;

/**
 * Clasa Episode reprezintă un episod dintr-un serial, incluzând titlul și numărul episodului.
 */
public class Episode {
    private String episodeName;
    private UUID episodeId;
    private int episodeNumber;

    /**
     * Constructor pentru clasa Episode.
     *
     * @param episodeName         Titlul episodului.
     * @param episodeNumber Numărul episodului.
     */
    public Episode(String episodeName, int episodeNumber) {
        this.episodeName = episodeName;
        this.episodeId = UUID.randomUUID();
        this.episodeNumber = episodeNumber;
    }

    /**
     * Returnează titlul episodului.
     *
     * @return Titlul episodului.
     */
    public String getEpisodeName() {
        return episodeName;
    }

    /**
     * Returnează ID-ul episodului.
     *
     * @return ID-ul episodului.
     */
    public UUID getEpisodeId() {
        return episodeId;
    }

    /**
     * Returnează numărul episodului.
     *
     * @return Numărul episodului.
     */
    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void nextEpisode() {
        this.episodeNumber++;

    }

}
