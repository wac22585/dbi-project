package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.Itinerary;
import at.spengergasse.backend.relational.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaItineraryRepository extends JpaRepository<Itinerary, Long>
{
    Itinerary findByUuid(UUID id);
    void deleteById(Long id);
    void deleteByUuid(UUID uuid);
    List<Itinerary> findAllByUser(User user);
}
