package org.stream.model.mappers;

import org.stream.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Clasa MovieMapper mappează datele din tabelul Movies intr-un obiect de tip Movie.
 */
public class MovieMapper implements Mapper<Movie> {

    private Connection connection;

    public MovieMapper(Connection connection) {
        this.connection = connection;
    }

    /**
     * Maparea rezultatului din baza de date într-un obiect Movie.
     *
     * @param resultSet Rezultatul interogării SQL.
     * @return Obiectul Movie care conține informațiile complete despre film.
     * @throws SQLException Dacă există erori în accesarea rezultatului.
     */
    @Override
    public Movie map(ResultSet resultSet) throws SQLException {
        UUID movieId = resultSet.getObject("id", UUID.class);
        String title = resultSet.getString("title");
        int duration = resultSet.getInt("duration");
        double rating = resultSet.getDouble("rating");

        Movie movie = new Movie(title, duration, rating);
        movie.setId(movieId);
        return movie;
    }

    /**
     * Obține un film după ID. Această metodă interoghează baza de date
     * pentru a obține detalii complete despre film.
     *
     * @param movieId ID-ul filmului.
     * @return Obiectul Movie corespunzător ID-ului.
     */
    public Movie getMovieById(UUID movieId) {
        String query = "SELECT id, title, duration, rating FROM Movies WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, movieId);

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

    @Override
    public String buildInsertSql() {
        return "INSERT INTO Movies (id, title, duration, rating) VALUES (?, ?, ?, ?)";
    }

    @Override
    public String buildUpdateSql() {
        return "UPDATE Movies SET title = ?, duration = ?, rating = ? WHERE id = ?";
    }

    @Override
    public void populateInsertStatement(PreparedStatement stmt, Movie movie) throws SQLException {
        stmt.setObject(1, movie.getId());
        stmt.setString(2, movie.getTitle());
        stmt.setInt(3, movie.getDuration());
        stmt.setDouble(4, movie.getRating());
    }

    @Override
    public void populateUpdateStatement(PreparedStatement stmt, Movie movie) throws SQLException {
        stmt.setString(1, movie.getTitle());
        stmt.setInt(2, movie.getDuration());
        stmt.setDouble(3, movie.getRating());
        stmt.setObject(4, movie.getId());
    }
}
