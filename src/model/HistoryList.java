package model;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HistoryList {
    private List<HistoryItem> history;

    public HistoryList() {
        this.history = new ArrayList<>();
    }

    public void addContent(String title, String type) {
        HistoryItem item = new HistoryItem(title, type, LocalDateTime.now());
        this.history.add(item);
    }

    public List<HistoryItem> getHistory() {
        return history;
    }

    public List<HistoryItem> getRecenteHistory(int limit) {
        int start = Math.max(history.size() - limit, 0);
        return history.subList(start, history.size());
    }
}
