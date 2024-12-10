package org.stream.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clasa HistoryItem reprezintă o intrare în istoricul de vizionare al utilizatorului,
 * incluzând titlul, tipul (Film sau Episod) și data vizionării.
 */
public class HistoryItem {
    private UUID id;
    private String title;
    private String type;
    private LocalDateTime viewedDate;

    /**
     * Constructor pentru clasa HistoryItem.
     *
     * @param title      Titlul conținutului vizionat.
     * @param type       Tipul conținutului ("Movie" sau "Episode").
     * @param viewedDate Data vizionării.
     */
    public HistoryItem(String title, String type, LocalDateTime viewedDate) {
        this.id = UUID.randomUUID(); // Generăm un ID unic automat
        this.title = title;
        this.type = type;
        this.viewedDate = viewedDate;
    }

    public UUID getId() {
        return id;
    }

    public String getContentType() {
        return type;
    }

    public void setId(UUID id) {
        this.id = id;
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
        return "ID: " + id + ", Title: " + title + ", Type: " + type + ", Viewed on: " + viewedDate;
    }
}
