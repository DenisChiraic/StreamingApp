package org.stream.model.mappers;

import org.stream.model.Episode;
import org.stream.model.Serial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clasa SerialMapper mappează datele din tabelul Serials într-un obiect Serial.
 */
public class SerialMapper implements Mapper<Serial> {
    private Connection connection;

    public SerialMapper(Connection connection) {
        this.connection = connection;
    }

    /**
     * Maparea rezultatului din baza de date într-un obiect Serial.
     *
     * @param resultSet Rezultatul interogării SQL.
     * @return Obiectul Serial care conține informațiile complete despre serial.
     * @throws SQLException Dacă există erori în accesarea rezultatului.
     */
    @Override
    public Serial map(ResultSet resultSet) throws SQLException {
        UUID serialId = resultSet.getObject("id", UUID.class);
        String title = resultSet.getString("title");
        double rating = resultSet.getDouble("rating");

        List<Episode> episodes = getEpisodesBySerialId(serialId);

        return new Serial(title, episodes, rating);
    }

    /**
     * Construiește interogarea SQL pentru inserarea unui serial.
     * @return SQL INSERT statement.
     */
    @Override
    public String buildInsertSql() {
        return "INSERT INTO Serial (id, title, rating) VALUES (?, ?, ?)";
    }

    /**
     * Construiește interogarea SQL pentru actualizarea unui serial.
     * @return SQL UPDATE statement.
     */
    @Override
    public String buildUpdateSql() {
        return "UPDATE Serial SET title = ?, rating = ? WHERE id = ?";
    }

    /**
     * Populează PreparedStatement pentru inserarea unui serial.
     *
     * @param stmt   PreparedStatement-ul.
     * @param serial Obiectul Serial.
     * @throws SQLException Dacă există erori în setarea valorilor.
     */
    @Override
    public void populateInsertStatement(PreparedStatement stmt, Serial serial) throws SQLException {
        stmt.setObject(1, serial.getId());
        stmt.setString(2, serial.getTitle());
        stmt.setDouble(3, serial.getRating());
    }

    /**
     * Populează PreparedStatement pentru actualizarea unui serial.
     *
     * @param stmt   PreparedStatement-ul.
     * @param serial Obiectul Serial.
     * @throws SQLException Dacă există erori în setarea valorilor.
     */
    @Override
    public void populateUpdateStatement(PreparedStatement stmt, Serial serial) throws SQLException {
        stmt.setString(1, serial.getTitle());
        stmt.setDouble(2, serial.getRating());
        stmt.setObject(3, serial.getId());
    }

    /**
     * Obține un serial după ID. Această metodă interoghează baza de date
     * pentru a obține detalii complete despre serial.
     *
     * @param serialId ID-ul serialului.
     * @return Obiectul Serial corespunzător ID-ului.
     */
    public Serial getSerialById(UUID serialId) {
        String query = "SELECT id, title, rating FROM Serial WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, serialId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Obține lista de episoade pentru un serial.
     *
     * @param serialId ID-ul serialului.
     * @return Lista de episoade.
     */
    public List<Episode> getEpisodesBySerialId(UUID serialId) {
        List<Episode> episodes = new ArrayList<>();
        String query = "SELECT id, episode_name, episode_number FROM Episode WHERE serial_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, serialId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String episodeName = rs.getString("episode_name");
                    UUID episodeId = rs.getObject("id", UUID.class);
                    int episodeNumber = rs.getInt("episode_number");

                    Episode episode = new Episode(serialId, episodeId, episodeName, episodeNumber);
                    episodes.add(episode);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return episodes;
    }
}
