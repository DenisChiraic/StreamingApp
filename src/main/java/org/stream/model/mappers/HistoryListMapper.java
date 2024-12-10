package org.stream.model.mappers;

import org.stream.model.HistoryItem;
import org.stream.model.HistoryList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Mapper pentru maparea datelor din baza de date într-un obiect HistoryList.
 */
public class HistoryListMapper implements Mapper<HistoryList> {
    private final Connection connection;

    public HistoryListMapper(Connection connection) {
        this.connection = connection;
    }

    /**
     * Maparea rezultatului din baza de date într-un obiect HistoryList.
     *
     * @param resultSet Rezultatul interogării SQL.
     * @return Obiectul HistoryList care conține lista de HistoryItem.
     * @throws SQLException Dacă există erori în accesarea rezultatului.
     */
    @Override
    public HistoryList map(ResultSet resultSet) throws SQLException {
        UUID listId = null;
        List<HistoryItem> historyItems = new ArrayList<>();

        while (resultSet.next()) {
            if (listId == null) {
                listId = resultSet.getObject("list_id", UUID.class); // ID-ul listei
            }

            UUID itemId = resultSet.getObject("id", UUID.class); // ID-ul HistoryItem
            String title = resultSet.getString("title");
            String contentType = resultSet.getString("content_type");
            Timestamp timestamp = resultSet.getTimestamp("viewed_at");

            HistoryItem historyItem = new HistoryItem(title, contentType, timestamp.toLocalDateTime());
            historyItem.setId(itemId); // Setăm ID-ul pentru HistoryItem
            historyItems.add(historyItem);
        }

        return new HistoryList(listId, historyItems);
    }

    /**
     * Construiește query-ul SQL pentru selectarea unei liste de istoric după ID-ul utilizatorului.
     *
     * @return Query-ul SQL.
     */
    public String buildSelectByUserIdSql() {
        return "SELECT * FROM HistoryList WHERE user_id = ?";
    }

    /**
     * Construiește query-ul SQL pentru inserarea unui HistoryItem.
     *
     * @return Query-ul SQL pentru inserare.
     */
    public String buildInsertSql() {
        return "INSERT INTO HistoryList (list_id, id, title, content_type, viewed_at) VALUES (?, ?, ?, ?, ?)";
    }

    /**
     * Construiește query-ul SQL pentru actualizarea unui HistoryItem.
     *
     * @return Query-ul SQL pentru actualizare.
     */
    @Override
    public String buildUpdateSql() {
        return "UPDATE HistoryList SET title = ?, content_type = ?, viewed_at = ? WHERE list_id = ? AND id = ?";
    }

    /**
     * Populează un PreparedStatement pentru inserarea unei liste de istoric.
     *
     * @param stmt   PreparedStatement-ul de populat.
     * @param entity Obiectul HistoryList de inserat.
     * @throws SQLException Dacă apare o eroare la popularea statement-ului.
     */
    @Override
    public void populateInsertStatement(PreparedStatement stmt, HistoryList entity) throws SQLException {
        for (HistoryItem item : entity.getHistory()) {
            stmt.setObject(1, entity.getId()); // ID-ul listei
            stmt.setObject(2, item.getId()); // ID-ul HistoryItem
            stmt.setString(3, item.getTitle());
            stmt.setString(4, item.getContentType());
            stmt.setTimestamp(5, Timestamp.valueOf(item.getViewedDate()));
            stmt.addBatch(); // Adaugă pentru execuție în batch
        }
    }

    /**
     * Populează un PreparedStatement pentru actualizarea unei liste de istoric.
     *
     * @param stmt   PreparedStatement-ul de populat.
     * @param entity Obiectul HistoryList de actualizat.
     * @throws SQLException Dacă apare o eroare la popularea statement-ului.
     */
    @Override
    public void populateUpdateStatement(PreparedStatement stmt, HistoryList entity) throws SQLException {
        for (HistoryItem item : entity.getHistory()) {
            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getContentType());
            stmt.setTimestamp(3, Timestamp.valueOf(item.getViewedDate()));
            stmt.setObject(4, entity.getId()); // ID-ul listei
            stmt.setObject(5, item.getId()); // ID-ul HistoryItem
            stmt.addBatch(); // Adaugă pentru execuție în batch
        }
    }
}
