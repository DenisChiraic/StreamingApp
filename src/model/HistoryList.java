package model;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Clasa HistoryList reprezintă istoricul de vizionare al utilizatorului,
 * stocând toate filmele și episoadele vizionate.
 */
public class HistoryList {
    private List<HistoryItem> history;

    /**
     * Constructor pentru HistoryList.
     */
    public HistoryList() {
        this.history = new ArrayList<>();
    }

    /**
     * Adaugă un element nou în istoricul de vizionare.
     *
     * @param title Titlul conținutului vizionat.
     * @param type  Tipul conținutului (Movie sau Episode).
     */
    public void addContent(String title, String type) {
        HistoryItem item = new HistoryItem(title, type, LocalDateTime.now());
        this.history.add(item);
    }

    /**
     * Returnează lista de elemente vizionate.
     *
     * @return Lista istoricului de vizionări.
     */
    public List<HistoryItem> getHistory() {
        return history;
    }

    /**
     * Returneaza doar o parte din HistoryList depinde de limit(Inca nu e implementat urmeaza)
     * @param limit
     * @return
     */
    public List<HistoryItem> getRecenteHistory(int limit) {
        int start = Math.max(history.size() - limit, 0);
        return history.subList(start, history.size());
    }

    /**
     * Afiseaza lista in forma de string element cu element
     * @return HistoryList
     */
    @Override
    public String toString() {
        StringBuilder historyStr = new StringBuilder("Viewing HistoryList:\n");
        for (HistoryItem item : history) {
            historyStr.append(item.toString()).append("\n");
        }
        return historyStr.toString();
    }
}
