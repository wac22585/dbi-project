package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.Itinerary;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface JpaItineraryRepository extends Repository<Itinerary, Long>
{
    Itinerary save(Itinerary itinerary);
    Itinerary findByUuid(UUID id);
    void deleteById(Long id);
    void deleteByUuid(UUID uuid);

    List<Itinerary> findAllByUserId(Long userId);
    List<Itinerary> findAll();
}
