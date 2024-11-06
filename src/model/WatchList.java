package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WatchList {
    private List<Objects> watchList;

    public WatchList() {
        this.watchList = new ArrayList<>();
    }

    public void addContent(Objects content) {
        watchList.add(content);
    }

    public void removeContent(Objects content) {
        watchList.remove(content);
    }

    public List<Objects> getWatchList() {
        return watchList;
    }
 }
