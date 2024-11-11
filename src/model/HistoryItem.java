package model;

import java.time.LocalDateTime;

/**
 * Clasa HistoryItem reprezintă o intrare în istoricul de vizionare al utilizatorului,
 * incluzând titlul, tipul (Film sau Episod) și data vizionării.
 */
public class HistoryItem {
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
        this.title = title;
        this.type = type;
        this.viewedDate = viewedDate;
    }

    /**
     * Returnează titlul conținutului vizionat.
     *
     * @return Titlul conținutului.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returnează tipul conținutului vizionat.
     *
     * @return Tipul conținutului ("Movie" sau "Episode").
     */
    public String getType() {
        return type;
    }

    /**
     * Returnează data vizionării.
     *
     * @return Data vizionării.
     */
    public LocalDateTime getViewedDate() {
        return viewedDate;
    }

    /**
     * Returnează o reprezentare string a obiectului HistoryItem.
     *
     * @return Reprezentarea string a istoricului de vizionare.
     */
    @Override
    public String toString() {
        return "Title: " + title + ", Type: " + type + ", Viewed on: " + viewedDate;
    }
}
