package org.stream.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfața generică IRepository definește operațiile de bază pentru un repository,
 * inclusiv crearea, citirea, actualizarea și ștergerea entităților.
 *
 * @param <T> Tipul entității gestionate de repository.
 */
public interface IRepo<T> {

    /**
     * Creează o nouă entitate în repository.
     *
     * @param obj Entitatea de adăugat.
     */
    void create(T obj);

    /**
     * Citește o entitate din repository pe baza ID-ului.
     *
     * @param id ID-ul entității de citit.
     * @return Un Optional care conține entitatea dacă este găsită, altfel gol.
     */
    Optional<T> read(UUID id);

    /**
     * Actualizează o entitate existentă în repository.
     *
     * @param obj Entitatea actualizată.
     */
    void update(T obj);

    /**
     * Șterge o entitate din repository pe baza ID-ului.
     *
     * @param id ID-ul entității de șters.
     */
    void delete(UUID id);

    /**
     * Returnează toate entitățile din repository.
     *
     * @return O listă cu toate entitățile.
     */
    List<T> findAll();
}
