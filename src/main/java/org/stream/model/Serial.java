package org.stream.model;

import java.util.List;
import java.util.UUID;

/**
 * Clasa Serial reprezintă un serial TV, incluzând titlul, lista de episoade și rating-ul.
 */
public class Serial implements Identifiable {
    private String title;
    private UUID serialId;
    private List<Episode> episodes;
    private double rating;

    /**
     * Constructor pentru clasa Serial.
     *
     * @param title    Titlul serialului.
     * @param episodes Lista de episoade ale serialului.
     * @param rating   Rating-ul serialului.
     */
    public Serial(String title, List<Episode> episodes, double rating) {
        this.title = title;
        this.serialId = UUID.randomUUID();
        this.episodes = episodes;
        this.rating = rating;
    }
    public Serial(UUID id, String title, List<Episode> episodes, double rating) {
        this.title = title;
        this.serialId = id;
        this.episodes = episodes;
        this.rating = rating;
    }
    /**
     * Returnează titlul serialului.
     *
     * @return Titlul serialului.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returnează rating-ul serialului.
     *
     * @return Rating-ul serialului.
     */
    public double getRating() {
        return rating;
    }

    /**
     * Returnează lista de episoade ale serialului.
     *
     * @return Lista de episoade.
     */
    public List<Episode> getEpisodes() {
        return episodes;
    }

    /**
     * Returnează ID-ul serialului.
     *
     * @return ID-ul serialului.
     */
    public UUID getId() {
        return serialId;
    }

    /**
     * Adaugă episodul vizionat în HistoryList
     *
     * @param historyList Lista de istorii de vizionare
     * @param episode Episodul care a fost vizionat
     */
    public void play(HistoryList historyList, Episode episode) {
        System.out.println("Now playing episode: " + episode.getEpisodeName() + " of " + title);
        historyList.addContent(episode.getEpisodeName(), "Serial");
    }

    /**
     * Mergem la următorul episod
     *
     * @param currentEpisode Episodul curent
     * @return Următorul episod sau null dacă nu mai există
     */
    public Episode nextEpisode(Episode currentEpisode) {
        if (episodes != null) {
            int currentIndex = episodes.indexOf(currentEpisode);
            if (currentIndex != -1 && currentIndex < episodes.size() - 1) {
                return episodes.get(currentIndex + 1);
            }
        }
        return null;
    }

    public void setId(UUID contentId) {
        this.serialId = contentId;
    }
}
