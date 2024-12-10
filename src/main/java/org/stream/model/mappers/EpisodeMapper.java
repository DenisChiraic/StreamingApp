package org.stream.model.mappers;

import org.stream.model.Episode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Clasa EpisodeMapper mappează datele din tabelul Episodes într-un obiect Episode.
 */
public class EpisodeMapper implements Mapper<Episode> {
    private final Connection connection;

    public EpisodeMapper(Connection connection) {
        this.connection = connection;
    }

    /**
     * Maparea rezultatului din baza de date într-un obiect Episode.
     *
     * @param resultSet Rezultatul interogării SQL.
     * @return Obiectul Episode care conține informațiile complete despre episod.
     * @throws SQLException Dacă există erori în accesarea rezultatului.
     */
    @Override
    public Episode map(ResultSet resultSet) throws SQLException {
        UUID episodeId = resultSet.getObject("id", UUID.class);
        UUID serialId = resultSet.getObject("serial_id", UUID.class);
        String episodeName = resultSet.getString("episode_name");
        int episodeNumber = resultSet.getInt("episode_number");

        return new Episode(serialId, episodeId, episodeName, episodeNumber);
    }

    /**
     * Construiește query-ul SQL pentru inserarea unui episod.
     *
     * @return String-ul SQL pentru inserare.
     */
    @Override
    public String buildInsertSql() {
        return "INSERT INTO Episodes (id, serial_id, episode_name, episode_number) VALUES (?, ?, ?, ?)";
    }

    /**
     * Construiește query-ul SQL pentru actualizarea unui episod.
     *
     * @return String-ul SQL pentru actualizare.
     */
    @Override
    public String buildUpdateSql() {
        return "UPDATE Episodes SET episode_name = ?, episode_number = ? WHERE id = ?";
    }

    /**
     * Populează un PreparedStatement pentru inserarea unui episod.
     *
     * @param stmt    PreparedStatement-ul de populat.
     * @param episode Episodul de inserat.
     * @throws SQLException Dacă apare o eroare la popularea statement-ului.
     */
    @Override
    public void populateInsertStatement(PreparedStatement stmt, Episode episode) throws SQLException {
        stmt.setObject(1, episode.getId());
        stmt.setObject(2, episode.getSerialId());
        stmt.setString(3, episode.getEpisodeName());
        stmt.setInt(4, episode.getEpisodeNumber());
    }

    /**
     * Populează un PreparedStatement pentru actualizarea unui episod.
     *
     * @param stmt    PreparedStatement-ul de populat.
     * @param episode Episodul de actualizat.
     * @throws SQLException Dacă apare o eroare la popularea statement-ului.
     */
    @Override
    public void populateUpdateStatement(PreparedStatement stmt, Episode episode) throws SQLException {
        stmt.setString(1, episode.getEpisodeName());
        stmt.setInt(2, episode.getEpisodeNumber());
        stmt.setObject(3, episode.getId());
    }
}
