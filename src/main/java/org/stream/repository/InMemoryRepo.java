package org.stream.repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Clasa InMemoryRepository implementează interfața IRepository și oferă
 * o stocare în memorie pentru entități, folosind un map pentru gestionarea datelor.
 *
 * @param <T> Tipul entității gestionate de repository.
 */
public class InMemoryRepo<T> implements IRepo<T> {
    private final Map<UUID, T> storage = new ConcurrentHashMap<>();

    /**
     * Creează o nouă entitate în repository, generând un UUID unic.
     *
     * @param obj Entitatea de adăugat.
     */
    @Override
    public void create(T obj) {
        storage.put(UUID.randomUUID(), obj);
    }

    /**
     * Citește o entitate din repository pe baza ID-ului.
     *
     * @param id ID-ul entității de citit.
     * @return Un Optional care conține entitatea dacă este găsită, altfel gol.
     */
    @Override
    public Optional<T> read(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    /**
     * Actualizează o entitate existentă în repository.
     *
     * @param obj Entitatea actualizată.
     */
    @Override
    public void update(T obj) {
        storage.put(UUID.randomUUID(), obj);
    }

    /**
     * Șterge o entitate din repository pe baza ID-ului.
     *
     * @param id ID-ul entității de șters.
     */
    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }

    /**
     * Returnează toate entitățile din repository.
     *
     * @return O listă cu toate entitățile.
     */
    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

}
