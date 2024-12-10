package org.stream.model;

import java.util.UUID;

/**
 * Clasa Episode reprezintă un episod dintr-un serial, incluzând titlul și numărul episodului.
 */
public class Episode {
    private UUID serialId;
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
    public Episode(UUID serialId, UUID episodeId, String episodeName, int episodeNumber) {
        this.serialId = serialId;
        this.episodeId = episodeId;
        this.episodeName = episodeName;
        this.episodeNumber = episodeNumber;
    }
    public UUID getSerialId() {
        return serialId;
    }
    /**
     * Returnează titlul episodului.
     *
     * @return Titlul episodului.
     */
    public String getEpisodeName() {
        return episodeName;
    }
    public UUID getId() {
        return episodeId;
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
