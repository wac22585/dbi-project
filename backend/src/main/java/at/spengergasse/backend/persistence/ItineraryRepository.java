package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.Itinerary;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ItineraryRepository extends Repository<Itinerary, Long>
{
    Itinerary save(Itinerary itinerary);
    Itinerary findById(Long id);
    void deleteById(Long id);

    List<Itinerary> findAllByUserId(Long userId);
    List<Itinerary> findAll();
}
