package model;

import java.time.LocalDateTime;

public class HistoryItem {
    private String title;
    private String type;
    private LocalDateTime viewedDate;

    public HistoryItem(String title, String type, LocalDateTime viewedDate) {
        this.title = title;
        this.type = type;
        this.viewedDate = viewedDate;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getViewedDate() {
        return viewedDate;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Type: " + type + ", Viewed on: " + viewedDate;
    }
}
