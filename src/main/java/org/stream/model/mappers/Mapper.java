package org.stream.model.mappers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interfața Mapper oferă metode pentru maparea dintre obiecte și baza de date.
 *
 * @param <T> Tipul obiectului gestionat.
 */
public interface Mapper<T> {

    /**
     * Mapează un rând din ResultSet la un obiect de tip T.
     *
     * @param resultSet ResultSet-ul din care se extrag datele.
     * @return Obiectul de tip T.
     * @throws SQLException Dacă apare o eroare la extragerea datelor.
     */
    T map(ResultSet resultSet) throws SQLException;

    /**
     * Construiește un query SQL pentru inserarea unui obiect.
     *
     * @return String-ul cu query-ul SQL pentru inserare.
     */
    String buildInsertSql();

    /**
     * Construiește un query SQL pentru actualizarea unui obiect.
     *
     * @return String-ul cu query-ul SQL pentru actualizare.
     */
    String buildUpdateSql();

    /**
     * Populează un PreparedStatement pentru inserarea unui obiect.
     *
     * @param stmt  PreparedStatement-ul de populat.
     * @param entity Entitatea de inserat.
     * @throws SQLException Dacă apare o eroare la populare.
     */
    void populateInsertStatement(PreparedStatement stmt, T entity) throws SQLException;

    /**
     * Populează un PreparedStatement pentru actualizarea unui obiect.
     *
     * @param stmt  PreparedStatement-ul de populat.
     * @param entity Entitatea de actualizat.
     * @throws SQLException Dacă apare o eroare la populare.
     */
    void populateUpdateStatement(PreparedStatement stmt, T entity) throws SQLException;
}
