package model;

import java.util.List;

public class Serial {
    private String title;
    private int SerialId;
    private List<Episode> episodes;

    public Serial(String serial, int SerialId, List<Episode> episodes) {
        this.title = serial;
        this.SerialId = SerialId;
        this.episodes = episodes;
    }

    public String getTitle() {
        return title;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public int getSerialId() {
        return SerialId;
    }

    public void play(HistoryList historyList) {
        System.out.println("Playing serial " + title);
        historyList.addContent(title, "Serial");
    }

    public void nextEpisode(Episode currentEpisode){
        currentEpisode.nextEpisode();

    }
}
