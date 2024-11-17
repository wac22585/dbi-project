package at.spengergasse.backend.relational.persistence;

import at.spengergasse.backend.relational.model.Itinerary;
import at.spengergasse.backend.relational.model.User;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface JpaItineraryRepository extends Repository<Itinerary, Long>
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
