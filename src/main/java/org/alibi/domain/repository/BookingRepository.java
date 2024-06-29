package org.alibi.domain.repository;

import org.alibi.domain.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    void save(Booking booking);
    Optional<Booking> findById(Long id);
    List<Booking> findAll();
    void update(Booking booking);
    void delete(Long id);
    boolean isConflict(Booking booking);
    void deleteAll();
}
