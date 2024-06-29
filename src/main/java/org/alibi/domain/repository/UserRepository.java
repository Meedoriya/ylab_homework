package org.alibi.domain.repository;

import org.alibi.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    void update(User user);
    void delete(Long id);

    void deleteAll();
}
