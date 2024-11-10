package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.Itinerary;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface ItineraryRepository extends Repository<Itinerary, Long>
{
    Itinerary save(Itinerary itinerary);
    Itinerary findByUuid(UUID id);
    void deleteById(Long id);
    void deleteByUuid(UUID uuid);

    List<Itinerary> findAllByUserId(Long userId);
    List<Itinerary> findAll();
}
