package repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRepo<T> implements IRepo<T> {
    private final Map<UUID, T> storage = new ConcurrentHashMap<>();

    @Override
    public void create(T obj) {
        storage.put(UUID.randomUUID(), obj);
    }

    @Override
    public Optional<T> read(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void update(T obj) {
        storage.put(UUID.randomUUID(), obj);
    }

    @Override
    public void delete(UUID id) {
        storage.remove(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

}
