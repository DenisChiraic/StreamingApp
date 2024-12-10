package org.stream.model.mappers;

import org.stream.model.Movie;
import org.stream.model.Serial;
import org.stream.model.WatchList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Clasa WatchListMapper este responsabilă pentru maparea datelor din baza de date într-un obiect WatchList.
 * Aceasta include filmele și serialele pe care utilizatorul le are în lista de vizionare.
 */
public class WatchListMapper implements Mapper<WatchList> {
    private Connection connection;
    private MovieMapper movieMapper;
    private SerialMapper serialMapper;

    /**
     * Constructor pentru WatchListMapper.
     *
     * @param connection   Conexiunea la baza de date.
     * @param movieMapper  Mapper pentru filme.
     * @param serialMapper Mapper pentru seriale.
     */
    public WatchListMapper(Connection connection, MovieMapper movieMapper, SerialMapper serialMapper) {
        this.connection = connection;
        this.movieMapper = movieMapper;
        this.serialMapper = serialMapper;
    }

    /**
     * Mapează rezultatele din baza de date într-un obiect WatchList.
     *
     * @param resultSet Rezultatul interogării SQL.
     * @return Obiectul WatchList care conține filmele și serialele asociate utilizatorului.
     * @throws SQLException Dacă există erori la procesarea rezultatelor.
     */
    @Override
    public WatchList map(ResultSet resultSet) throws SQLException {
        WatchList watchList = new WatchList();

        while (resultSet.next()) {
            String contentType = resultSet.getString("content_type");
            UUID contentId = (UUID) resultSet.getObject("content_id");

            if ("Movie".equals(contentType)) {
                Movie movie = movieMapper.getMovieById(contentId);
                if (movie != null) {
                    watchList.addMovie(movie);
                }
            }
            else if ("Serial".equals(contentType)) {
                Serial serial = serialMapper.getSerialById(contentId);
                if (serial != null) {
                    watchList.addSerial(serial);
                }
            }
        }

        return watchList;
    }

    /**
     * Construiește interogarea SQL pentru inserarea unui element în lista de vizionare.
     * @return SQL INSERT statement.
     */
    @Override
    public String buildInsertSql() {
        return "INSERT INTO WatchList (user_id, content_id, content_type) VALUES (?, ?, ?)";
    }

    /**
     * Construiește interogarea SQL pentru actualizarea listei de vizionare a unui utilizator.
     * @return SQL UPDATE statement.
     */
    @Override
    public String buildUpdateSql() {
        return "UPDATE WatchList SET content_type = ? WHERE user_id = ? AND content_id = ?";
    }

    /**
     * Populează un PreparedStatement pentru inserarea unui element în lista de vizionare.
     * @param stmt   PreparedStatement-ul.
     * @param entity Obiectul WatchList.
     * @throws SQLException Dacă există erori la setarea valorilor.
     */
    @Override
    public void populateInsertStatement(PreparedStatement stmt, WatchList entity) throws SQLException {
        for (Movie movie : entity.getMovies()) {
            stmt.setObject(1, entity.getUserId());
            stmt.setObject(2, movie.getId());
            stmt.setString(3, "Movie");
            stmt.addBatch();
        }

        for (Serial serial : entity.getSerials()) {
            stmt.setObject(1, entity.getUserId());
            stmt.setObject(2, serial.getId());
            stmt.setString(3, "Serial");
            stmt.addBatch();
        }
    }

    /**
     * Populează un PreparedStatement pentru actualizarea unui element în lista de vizionare.
     * @param stmt   PreparedStatement-ul.
     * @param entity Obiectul WatchList.
     * @throws SQLException Dacă există erori la setarea valorilor.
     */
    @Override
    public void populateUpdateStatement(PreparedStatement stmt, WatchList entity) throws SQLException {
        for (Movie movie : entity.getMovies()) {
            stmt.setString(1, "Movie");
            stmt.setObject(2, entity.getUserId());
            stmt.setObject(3, movie.getId());
            stmt.addBatch();
        }

        for (Serial serial : entity.getSerials()) {
            stmt.setString(1, "Serial");
            stmt.setObject(2, entity.getUserId());
            stmt.setObject(3, serial.getId());
            stmt.addBatch();
        }
    }
}
