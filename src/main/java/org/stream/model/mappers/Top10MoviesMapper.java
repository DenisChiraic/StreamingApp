package org.stream.model.mappers;

import org.stream.model.Movie;
import org.stream.model.TopList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clasa Top10MoviesMapper mappează datele din tabelul Top10Movies într-un obiect TopList.
 */
public class Top10MoviesMapper implements Mapper<TopList> {
    private Connection connection;
    private MovieMapper movieMapper;

    /**
     * Constructor pentru Top10MoviesMapper.
     *
     * @param connection   Conexiunea la baza de date.
     * @param movieMapper  Mapper pentru filme.
     */
    public Top10MoviesMapper(Connection connection, MovieMapper movieMapper) {
        this.connection = connection;
        this.movieMapper = movieMapper;
    }

    /**
     * Maparea rezultatului din baza de date într-un obiect TopList.
     *
     * @param resultSet Rezultatul interogării SQL.
     * @return Obiectul TopList care conține lista de filme.
     * @throws SQLException Dacă există erori în accesarea rezultatului.
     */
    @Override
    public TopList map(ResultSet resultSet) throws SQLException {
        List<Movie> movies = new ArrayList<>();

        while (resultSet.next()) {
            UUID movieId = resultSet.getObject("movie_id", UUID.class);
            double rating = resultSet.getDouble("rating");

            Movie movie = movieMapper.getMovieById(movieId);

            if (movie != null) {
                movies.add(movie);
            }
        }

        TopList topList = new TopList();
        topList.updateTopMovies(movies);

        return topList;
    }

    /**
     * Construiește interogarea SQL pentru inserarea unui film în top.
     * @return SQL INSERT statement.
     */
    public String buildInsertSql() {
        return "INSERT INTO Top10Movies (movie_id, rating) VALUES (?, ?)";
    }

    /**
     * Construiește interogarea SQL pentru actualizarea unui film în top.
     * @return SQL UPDATE statement.
     */
    public String buildUpdateSql() {
        return "UPDATE Top10Movies SET rating = ? WHERE movie_id = ?";
    }

    /**
     * Populează PreparedStatement pentru inserarea unui film în top.
     *
     * @param stmt   PreparedStatement-ul.
     * @param entity Obiectul TopList (care conține filmele).
     * @throws SQLException Dacă există erori în setarea valorilor.
     */
    public void populateInsertStatement(PreparedStatement stmt, TopList entity) throws SQLException {
        for (Movie movie : entity.getMovies()) {
            stmt.setObject(1, movie.getId());
            stmt.setDouble(2, movie.getRating());
            stmt.addBatch();
        }
    }

    /**
     * Populează PreparedStatement pentru actualizarea unui film în top.
     *
     * @param stmt   PreparedStatement-ul.
     * @param entity Obiectul TopList (care conține filmele).
     * @throws SQLException Dacă există erori în setarea valorilor.
     */
    public void populateUpdateStatement(PreparedStatement stmt, TopList entity) throws SQLException {
        for (Movie movie : entity.getMovies()) {
            stmt.setDouble(1, movie.getRating());
            stmt.setObject(2, movie.getId());
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
        String query = "SELECT movie_id, rating FROM Top10Movies WHERE top_id = ?";
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
