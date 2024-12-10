package org.stream.repository;

import org.stream.config.DBConfig;
import org.stream.model.Identifiable;
import org.stream.model.exceptions.DatabaseException;
import org.stream.model.mappers.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DatabaseRepo<T extends Identifiable> implements IRepo<T> {
    private final String tableName;
    private final Mapper<T> mapper;
    private final String dbUrl = DBConfig.DB_URL;
    private final String dbUser = DBConfig.DB_USER;
    private final String dbPassword = DBConfig.DB_PASSWORD;

    public DatabaseRepo(String tableName, Mapper<T> mapper) {
        this.tableName = tableName;
        this.mapper = mapper;
    }

    private Connection getConnection() throws DatabaseException {
        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new DatabaseException("Nu s-a putut conecta la baza de date.");
        }
    }

    public void save(T obj) {
        Optional<T> existingEntity = read(obj.getId());
        if (existingEntity.isPresent()) {
            update(obj);
        } else {
            create(obj);
        }
    }

    @Override
    public void create(T obj) {
        String sql = mapper.buildInsertSql();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            mapper.populateInsertStatement(stmt, obj);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    UUID id = (UUID) rs.getObject(1);
                    obj.setId(id);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la crearea entității.");
        }
    }

    @Override
    public Optional<T> read(UUID id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T entity = mapper.map(rs);
                    return Optional.of(entity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la citirea entității.");
        }
    }

    @Override
    public void update(T obj) {
        String sql = mapper.buildUpdateSql();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            mapper.populateUpdateStatement(stmt, obj);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la actualizarea entității.");
        }
    }

    @Override
    public void delete(UUID id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la ștergerea entității.");
        }
    }

    @Override
    public List<T> findAll() {
        String sql = "SELECT * FROM " + tableName;
        List<T> entities = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                T entity = mapper.map(rs);
                entities.add(entity);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Eroare la citirea entităților.");
        }
        return entities;
    }
}
