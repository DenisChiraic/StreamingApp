package org.stream.model.mappers;

import org.stream.model.Serial;
import org.stream.model.TopList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clasa Top10SerialsMapper mappează datele din tabelul Top10Serials într-un obiect TopList.
 */
public class Top10SerialsMapper implements Mapper<TopList> {
    private Connection connection;
    private SerialMapper serialMapper;

    /**
     * Constructor pentru Top10SerialsMapper.
     *
     * @param connection   Conexiunea la baza de date.
     * @param serialMapper Mapper pentru seriale.
     */
    public Top10SerialsMapper(Connection connection, SerialMapper serialMapper) {
        this.connection = connection;
        this.serialMapper = serialMapper;
    }

    /**
     * Maparea rezultatului din baza de date într-un obiect TopList.
     *
     * @param resultSet Rezultatul interogării SQL.
     * @return Obiectul TopList care conține lista de seriale.
     * @throws SQLException Dacă există erori în accesarea rezultatului.
     */
    @Override
    public TopList map(ResultSet resultSet) throws SQLException {
        List<Serial> serials = new ArrayList<>();

        while (resultSet.next()) {
            UUID serialId = resultSet.getObject("serial_id", UUID.class);
            double rating = resultSet.getDouble("rating");

            Serial serial = serialMapper.getSerialById(serialId);

            if (serial != null) {
                serials.add(serial);
            }
        }

        TopList topList = new TopList();
        topList.updateTopSerials(serials);

        return topList;
    }

    /**
     * Construiește interogarea SQL pentru inserarea unui serial în top.
     * @return SQL INSERT statement.
     */
    public String buildInsertSql() {
        return "INSERT INTO Top10Serials (serial_id, rating) VALUES (?, ?)";
    }

    /**
     * Construiește interogarea SQL pentru actualizarea unui serial în top.
     * @return SQL UPDATE statement.
     */
    public String buildUpdateSql() {
        return "UPDATE Top10Serials SET rating = ? WHERE serial_id = ?";
    }

    /**
     * Populează PreparedStatement pentru inserarea unui serial în top.
     *
     * @param stmt   PreparedStatement-ul.
     * @param entity Obiectul TopList (care conține serialele).
     * @throws SQLException Dacă există erori în setarea valorilor.
     */
    public void populateInsertStatement(PreparedStatement stmt, TopList entity) throws SQLException {
        for (Serial serial : entity.getSerials()) {
            stmt.setObject(1, serial.getId());
            stmt.setDouble(2, serial.getRating());
            stmt.addBatch();
        }
    }

    /**
     * Populează PreparedStatement pentru actualizarea unui serial în top.
     *
     * @param stmt   PreparedStatement-ul.
     * @param entity Obiectul TopList (care conține serialele).
     * @throws SQLException Dacă există erori în setarea valorilor.
     */
    public void populateUpdateStatement(PreparedStatement stmt, TopList entity) throws SQLException {
        for (Serial serial : entity.getSerials()) {
            stmt.setDouble(1, serial.getRating());
            stmt.setObject(2, serial.getId());
            stmt.addBatch();
        }
    }

    /**
     * Obține un TopList din baza de date.
     *
     * @param topId ID-ul topului.
     * @return Obiectul TopList corespunzător ID-ului.
     */
    public TopList getTopListById(UUID topId) {
        String query = "SELECT serial_id, rating FROM Top10Serials WHERE top_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, topId);

            try (ResultSet rs = stmt.executeQuery()) {
                return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
