package org.stream.model.mappers;

import org.stream.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Clasa UserMapper mappează datele dintr-un ResultSet într-un obiect de tip User.
 * Este folosită pentru a transforma datele extrase dintr-o bază de date într-un obiect User.
 */
public class UserMapper implements Mapper<User> {

    /**
     * Mappează un rând din ResultSet într-un obiect User.
     * Această metodă extrage valorile relevante din ResultSet și creează un obiect User.
     *
     * @param resultSet Rezultatul interogării SQL care conține datele utilizatorului.
     * @return Un obiect de tip User care reprezintă un utilizator.
     * @throws SQLException Dacă există erori în accesarea datelor din ResultSet.
     */
    @Override
    public User map(ResultSet resultSet) throws SQLException {
        UUID id = UUID.fromString(resultSet.getString("id"));
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        boolean isAdmin = resultSet.getBoolean("is_admin");

        return new User(id, username, password, isAdmin);
    }

    /**
     * Construiește interogarea SQL pentru inserarea unui utilizator în baza de date.
     * @return SQL INSERT statement.
     */
    @Override
    public String buildInsertSql() {
        return "INSERT INTO Users (id, username, password, is_admin) VALUES (?, ?, ?, ?)";
    }

    /**
     * Construiește interogarea SQL pentru actualizarea unui utilizator existent în baza de date.
     * @return SQL UPDATE statement.
     */
    @Override
    public String buildUpdateSql() {
        return "UPDATE Users SET username = ?, password = ?, is_admin = ? WHERE id = ?";
    }

    /**
     * Populează un PreparedStatement pentru inserarea unui utilizator în baza de date.
     * @param stmt   PreparedStatement-ul.
     * @param entity Obiectul User.
     * @throws SQLException Dacă există erori la setarea valorilor.
     */
    @Override
    public void populateInsertStatement(PreparedStatement stmt, User entity) throws SQLException {
        stmt.setObject(1, entity.getId());
        stmt.setString(2, entity.getUsername());
        stmt.setString(3, entity.getPassword());
        stmt.setBoolean(4, entity.isAdmin());
    }

    /**
     * Populează un PreparedStatement pentru actualizarea unui utilizator existent în baza de date.
     * @param stmt   PreparedStatement-ul.
     * @param entity Obiectul User.
     * @throws SQLException Dacă există erori la setarea valorilor.
     */
    @Override
    public void populateUpdateStatement(PreparedStatement stmt, User entity) throws SQLException {
        stmt.setString(1, entity.getUsername());
        stmt.setString(2, entity.getPassword());
        stmt.setBoolean(3, entity.isAdmin());
        stmt.setObject(4, entity.getId());
    }
}
