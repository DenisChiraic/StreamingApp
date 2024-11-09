package repository;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

public interface IRepo<T> {
    void create(T obj);
    Optional<T> read(UUID id);
    void update(T obj);
    void delete(UUID id);
    List<T> findAll();
}
