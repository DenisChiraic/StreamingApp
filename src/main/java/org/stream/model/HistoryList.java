package org.stream.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clasa HistoryList reprezintă istoricul de vizionare al utilizatorului,
 * stocând toate filmele și episoadele vizionate.
 */
public class HistoryList {
    private UUID id; // ID unic pentru listă
    private List<HistoryItem> history;

    /**
     * Constructor pentru HistoryList fără ID specificat (se generează unul automat).
     */
    public HistoryList() {
        this.id = UUID.randomUUID();
        this.history = new ArrayList<>();
    }


    /**
     * Constructor pentru HistoryList cu ID și listă.
     */
    public HistoryList(UUID id, List<HistoryItem> history) {
        this.id = id;
        this.history = history;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<HistoryItem> getHistory() {
        return history;
    }

    public void displayHistory() {
        if (history.isEmpty()) {
            System.out.println("History is empty.");
        } else {
            System.out.println("History:");
            for (HistoryItem entry : history) {
                System.out.println("- " + entry);
            }
        }
    }

    /**
     * Adaugă un element nou în istoricul de vizionare.
     *
     * @param title Titlul conținutului vizionat.
     * @param type  Tipul conținutului (Movie sau Episode).
     */
    public void addContent(String title, String type) {
        HistoryItem item = new HistoryItem(title, type, java.time.LocalDateTime.now());
        this.history.add(item);
    }

    /**
     * Returnează o parte din HistoryList în funcție de un limitator.
     *
     * @param limit Numărul de elemente de returnat.
     * @return Sublista cu ultimele elemente.
     */
    public List<HistoryItem> getRecenteHistory(int limit) {
        int start = Math.max(history.size() - limit, 0);
        return history.subList(start, history.size());
    }

    @Override
    public String toString() {
        StringBuilder historyStr = new StringBuilder("Viewing HistoryList (ID: " + id + "):\n");
        for (HistoryItem item : history) {
            historyStr.append(item.toString()).append("\n");
        }
        return historyStr.toString();
    }
}
