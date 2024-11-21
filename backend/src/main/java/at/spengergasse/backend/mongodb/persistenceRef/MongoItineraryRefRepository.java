package at.spengergasse.backend.mongodb.persistenceRef;

import at.spengergasse.backend.mongodb.modelRef.Itinerary;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface MongoItineraryRefRepository extends MongoRepository<Itinerary, String> {

    List<Itinerary> findAll();
    Itinerary findByUuid(UUID uuid);
    Itinerary save(Itinerary entity);
    void deleteById(String id);
    void deleteByUuid(UUID uuid);
}