package at.spengergasse.backend.persistence;

import at.spengergasse.backend.model.Itinerary;
import at.spengergasse.backend.model.User;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface ItineraryRepository extends Repository<Itinerary, Long>
{
    Itinerary save(Itinerary itinerary);
    Itinerary findByUuid(UUID id);
    //Itinerary findByName(String name);
    void deleteById(Long id);
    void deleteByUuid(UUID uuid);

    // Long UserId -> User user
    List<Itinerary> findAllByUser(User user);
    List<Itinerary> findAll();
}
