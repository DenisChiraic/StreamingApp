package model;

import java.util.List;
import java.util.UUID;

/**
 * Clasa Serial reprezintă un serial TV, incluzând titlul, lista de episoade și rating-ul.
 */
public class Serial {
    private String title;
    private UUID serialId;
    private List<Episode> episodes;
    private double rating;

    /**
     * Constructor pentru clasa Serial.
     *
     * @param serial   Titlul serialului.
     * @param episodes Lista de episoade ale serialului.
     * @param rating   Rating-ul serialului.
     */
    public Serial(String serial, List<Episode> episodes, double rating) {
        this.title = serial;
        this.serialId = UUID.randomUUID();
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
    public UUID getSerialId() {
        return serialId;
    }

    /**
     * Adauga episod-ul vazut in HistoryList
     * @param historyList
     * @param episode
     */
    public void play(HistoryList historyList, Episode episode) {
        System.out.println("Now playing episode: " + episode.getEpisodeName() + " of " + title);
        historyList.addContent(episode.getEpisodeName(), "Serial");
    }

    /**
     * Mergem la alt episode (Inca nu am implementat o sa mearga pe viitor)
     * @param currentEpisode Episodul curent
     * @return Urmatorul episod sau null daca nu mai exista
     */
    public Episode nextEpisode(Episode currentEpisode){
        int currentIdex = episodes.indexOf(currentEpisode);
        if (currentIdex != -1 && currentIdex < episodes.size() - 1) {
            return episodes.get(currentIdex + 1);
        }
        return null;
    }
}
